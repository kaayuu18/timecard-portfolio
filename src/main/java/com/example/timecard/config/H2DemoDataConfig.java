package com.example.timecard.config;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import com.example.timecard.entity.Person;
import com.example.timecard.entity.Kintai;
import com.example.timecard.entity.Shift;
import com.example.timecard.repository.PersonRepository;
import com.example.timecard.repository.KintaiRepository;
import com.example.timecard.repository.ShiftRepository;
import com.example.timecard.service.KintaiService;

@Configuration
@Profile("h2")
public class H2DemoDataConfig {
    @Bean
    ApplicationRunner initializeH2DemoData(PersonRepository people,
            KintaiRepository attendance, ShiftRepository shifts,
            KintaiService calculations, PasswordEncoder encoder,
            JdbcTemplate jdbc, PlatformTransactionManager transactionManager) {
        return args -> new TransactionTemplate(transactionManager).executeWithoutResult(status -> {
            // 新しいDBだけ初期ログインを作る。既存アカウントは変更しない。
            if (people.count() == 0) {
                people.saveAndFlush(person("admin", "ADMIN", 1000, encoder));
                people.saveAndFlush(person("demo", "USER", 1000, encoder));
            }
            // 一度追加したサンプルは、再起動しても重複させない。
            jdbc.execute("CREATE TABLE IF NOT EXISTS h2_demo_seed (seed_key VARCHAR(64) PRIMARY KEY)");
            Integer seeded = jdbc.queryForObject(
                    "SELECT COUNT(*) FROM h2_demo_seed WHERE seed_key = ?", Integer.class, "sample-v2");
            if (seeded != null && seeded > 0) return;

            String[] names = {"サンプル_山田太郎", "サンプル_佐藤花子", "サンプル_鈴木一郎",
                    "サンプル_田中美咲", "サンプル_高橋健太"};
            int[] wages = {1100, 1200, 1300, 1400, 1500};
            List<Person> workers = new ArrayList<>();
            for (int i = 0; i < names.length; i++) {
                // 同名の人が既にいても、その人の情報には触れない。
                String name = names[i];
                int suffix = 2;
                while (people.findByName(name).isPresent()) name = names[i] + "_" + suffix++;
                workers.add(people.saveAndFlush(person(name, "USER", wages[i], encoder)));
            }
            LocalDate today = LocalDate.now();
            for (Person worker : workers) {
                // 過去6日分。通常勤務・短時間勤務・残業の例を用意する。
                for (int day = 1; day <= 6; day++) {
                    Kintai record = new Kintai();
                    record.setPerson(worker);
                    record.setDate(today.minusDays(day));
                    record.setGo(LocalTime.of(9, 0));
                    record.setBreakStartTime(LocalTime.of(12, 0));
                    record.setBreakEndTime(LocalTime.of(12, day % 3 == 1 ? 30 : 0)
                            .plusHours(day % 3 == 1 ? 0 : 1));
                    record.setOut(day % 3 == 1 ? LocalTime.of(14, 0)
                            : day % 3 == 2 ? LocalTime.of(19, 30) : LocalTime.of(17, 0));
                    calculations.breakTime(record);
                    calculations.workingHours(record);
                    calculations.todayWage(record);
                    attendance.saveAndFlush(record);
                }
                // 今日から7日分の予定。休憩の単位は分。
                for (int day = 0; day < 7; day++) {
                    Shift shift = new Shift();
                    shift.setPerson(worker);
                    shift.setDate(today.plusDays(day));
                    shift.setGo(LocalTime.of(9, 0));
                    shift.setOut(LocalTime.of(17, 0));
                    shift.setBreakTime(60.0);
                    shifts.saveAndFlush(shift);
                }
            }
            // 今日の打刻一覧にも、出勤中のサンプルを2人表示する。
            for (int i = 0; i < 2; i++) {
                Kintai record = new Kintai();
                record.setPerson(workers.get(i));
                record.setDate(today);
                record.setGo(LocalTime.now().withSecond(0).withNano(0));
                attendance.saveAndFlush(record);
            }
            jdbc.update("INSERT INTO h2_demo_seed (seed_key) VALUES (?)", "sample-v2");
        });
    }

    private Person person(String name, String role, int wage, PasswordEncoder encoder) {
        Person person = new Person();
        person.setName(name);
        person.setPassword(encoder.encode("demo1234"));
        person.setRole(role);
        person.setEnable(true);
        person.setAge(30);
        person.setHourWage(wage);
        person.setMail("sample@example.invalid");
        person.setPhoneNum("00000000000");
        person.setAddress("サンプル住所");
        return person;
    }
}

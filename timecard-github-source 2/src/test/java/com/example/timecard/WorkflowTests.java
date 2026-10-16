package com.example.timecard;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import com.example.timecard.repository.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties="spring.datasource.url=jdbc:h2:mem:workflow;DB_CLOSE_DELAY=-1;MODE=MySQL")
@AutoConfigureMockMvc
@Transactional
class WorkflowTests {
 @Autowired MockMvc mvc;
 @Autowired PersonRepository people;
 @Autowired ShiftRepository shifts;
 @Autowired KintaiRepository attendance;
 @Test void punchFlowRejectsDuplicatesAndInvalidOrder() throws Exception {
  String id=people.findByName("demo").orElseThrow().getId().toString();
  mvc.perform(post("/timecard").param("person",id).param("action","out").with(csrf())).andExpect(status().isOk()).andExpect(model().attributeExists("kintaierror"));
  for(String action:new String[]{"go","breakstart","breakend","out"})
   mvc.perform(post("/timecard").param("person",id).param("action",action).with(csrf())).andExpect(status().is3xxRedirection());
  mvc.perform(post("/timecard").param("person",id).param("action","go").with(csrf())).andExpect(model().attributeExists("kintaierror"));
  assertEquals(1,attendance.findAll().stream().filter(k->k.getPerson().getId().toString().equals(id)).count());
 }
 @Test void csrfRequired() throws Exception {
  mvc.perform(post("/timecard").param("person","1").param("action","go")).andExpect(status().isForbidden());
 }
 @Test @WithMockUser(roles="ADMIN") void shiftCrudAndValidation() throws Exception {
  String person=people.findByName("demo").orElseThrow().getId().toString();
  long before=shifts.count();
  mvc.perform(get("/shiftedit")).andExpect(status().isOk());
  mvc.perform(post("/admin/shifts/save").with(csrf()).param("person",person).param("date",LocalDate.now().toString()).param("go","09:00").param("out","08:00")).andExpect(status().is3xxRedirection());
  assertEquals(before,shifts.count());
  mvc.perform(post("/admin/shifts/save").with(csrf()).param("person",person).param("date",LocalDate.now().toString()).param("go","09:00").param("out","18:00").param("breakMinutes","60")).andExpect(status().is3xxRedirection());
  var shift=shifts.findAll().stream().filter(s->s.getPerson().getId().toString().equals(person)).findFirst().orElseThrow();
  mvc.perform(post("/admin/shifts/save").with(csrf()).param("id",shift.getId().toString()).param("person",person).param("date",LocalDate.now().toString()).param("go","10:00").param("out","18:00").param("breakMinutes","30")).andExpect(status().is3xxRedirection());
  assertEquals(30.0,shifts.findById(shift.getId()).orElseThrow().getBreakTime());
  mvc.perform(get("/admin/salaryinfo").param("id",person)).andExpect(status().isOk());
  mvc.perform(post("/admin/shifts/delete").with(csrf()).param("id",shift.getId().toString())).andExpect(status().is3xxRedirection());
  assertEquals(before,shifts.count());
 }
 @Test @WithMockUser(roles="USER") void employeeCannotChangeShifts() throws Exception {
  mvc.perform(post("/admin/shifts/delete").with(csrf()).param("id","1")).andExpect(status().isForbidden());
 }
}

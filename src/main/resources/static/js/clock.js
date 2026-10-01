// 現在の日付と時刻の表示を更新し、打刻画面の表示操作を行う。
timerID=setInterval(clock,500);//トリガーやと思う5ミリ秒後ごとに表示？

function clock(){
         document.getElementById("view_clock").innerHTML=getNow();
         document.getElementById("view_date").innerHTML=getDate();
         }
function getDate(){
         var now = new Date();
         var month = now.getMonth() +1; //月
         var day = now.getDate();//日にち
         var date = month + "月" + day + "日";
         return date;
}
function getNow(){
         var now = new Date();
         var hour = now.getHours();//時間
         var min = now.getMinutes();//分
         var sec = now.getSeconds();//秒を取得
         var time = hour + ":" + min + ":" + sec;
         return time;
}
function showkintai(){
         //event.preventDefault();
         const out = document.getElementById('person').value;
         document.getElementById('outshow').textContent = out;
         const dialog = document.getElementById('show');
         dialog.show();
}
function closedialog(){
         const dialog = document.getElementById('show');
         dialog.close();
}

// 選択した行の内容を編集・削除ダイアログに入れ、フォームを送信する。

let row = "";//グローバル変数にする必要がある
//----------------------------------------------------------
function edit(event){

event.preventDefault();
document.getElementById('dialog').style.display = "block";
row = event.target.closest('tr');//console.log(row);で<tr></tr>とその中にあるものを表示

document.getElementById('editid').value = row.cells[1].innerText;
document.getElementById('editname').value = row.cells[2].innerText;
document.getElementById('editgo').value = row.cells[3].innerText;
document.getElementById('editout').value = row.cells[4].innerText;
document.getElementById('editbreaktime').value = row.cells[5].innerText;
document.getElementById("dialog").style.display = "block";
}
//閉じる
function closedialog(){
document.getElementById('dialog').style.display ="none";
}

//--------------------------------------------------------------------------------------
function remove(event){

event.preventDefault();
row = event.target.closest('tr');//console.log(row);で<tr></tr>とその中にあるものを表示

document.getElementById('removeid').value = row.cells[1].innerText;
document.getElementById('removename').value = row.cells[2].innerText;
document.getElementById('removego').value = row.cells[3].innerText;
document.getElementById('removeout').value = row.cells[4].innerText;
document.getElementById('removebreaktime').value = row.cells[5].innerText;
document.getElementById("removedialog").style.display = "block";
}
//閉じる
function closedeletedialog(){
document.getElementById('removedialog').style.display ="none";
}
//----------------------------------シフトの編集ダイアログ------------------------------------------------
function editshift(event){

event.preventDefault();
document.getElementById('shifteditdialog').style.display = "block";
row = event.target.closest('tr');//console.log(row);で<tr></tr>とその中にあるものを表示

document.getElementById('shifteditid').value = row.cells[1].innerText;
document.getElementById('shifteditname').value = row.cells[2].innerText;
document.getElementById('shifteditgo').value = row.cells[3].innerText;
document.getElementById('shifteditout').value = row.cells[4].innerText;
document.getElementById('shifteditbreaktime').value = row.cells[5].innerText;
document.getElementById("shifteditdialog").style.display = "block";
}
//閉じる
function editshiftclose(){
document.getElementById("shifteditdialog").style.display = "none";
}
//--------------------------------------------------------------------------------------------------------
//----------------------------シフトを削除(12/3)
function removeshift(event){

event.preventDefault();
row = event.target.closest('tr');//console.log(row);で<tr></tr>とその中にあるものを表示

document.getElementById('shiftremoveid').value = row.cells[1].innerText;
document.getElementById('shiftremovename').value = row.cells[2].innerText;
document.getElementById('shiftremovego').value = row.cells[3].innerText;
document.getElementById('shiftremoveout').value = row.cells[4].innerText;
document.getElementById('shiftremovebreaktime').value = row.cells[5].innerText;
document.getElementById("shiftremovedialog").style.display = "block";
}
//閉じる
function closeshifteditdialog(){
document.getElementById('shiftremovedialog').style.display ="none";
}
//----------------------------------------------------------------------------------------------------------

//----------------------------
function editinfo(event){
console.log(event.target.innerText);//更新って表示。ボタンに表示されているものを表示すする
//------------ダイアログで書かれたものをセルに追加; /////
console.log(document.getElementById('editname').value);
row.cells[2].innerText = document.getElementById('editname').value;
row.cells[3].innerText = document.getElementById('editgo').value;
row.cells[4].innerText = document.getElementById('editout').value;
row.cells[5].innerText = document.getElementById('editbreaktime').value;
const form = document.getElementById('form');
form.submit();
// document.getElementById('dialog').style.display ="none";
}
//-------------------------------------------------------------------------------------
function shiftedit(event){
console.log("ボタンが押されました");
event.preventDefault();
document.getElementById('addshiftdialog').style.display="block";
} 
function closeshiftdialog(){
document.getElementById('addshiftdialog').style.display ="none";
}
//--------------------------------------------------------------------------------------
//----------------------------------------------------     
function kintaiedit(event){
console.log("勤怠きろく追加ボタンが押されました");
event.preventDefault();
document.getElementById('kintaidialog').style.display="block";
}
function closekintaidialog(){
document.getElementById('kintaidialog').style.display = "none";
}
//------------------------------------------------------------




function senddate(){
document.getElementById('inputdate').value = document.getElementById('date').value; 
}
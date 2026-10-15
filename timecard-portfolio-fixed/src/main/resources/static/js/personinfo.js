// 従業員の編集・削除の確認ダイアログを開閉する。
function deletecheck(event){
    event.preventDefault();       
    document.getElementById('dialogpeopleid').innerText = document.getElementById('id').value;
    document.getElementById('dialogpeoplename').innerText = document.getElementById('name').value;
    document.getElementById('dialog').style.display = "block";
    console.log(document.getElementById('name').value);

}
function deletesubmit(){
    const form = document.getElementById('deleteform');
    form.submit();


}
function closeDialog(){
    document.getElementById('dialog').style.display = "none";
    
}
function editdialogopen(){
    document.getElementById('edit-overlay').style.display = "block";

}
function closeeditdialog(){
    document.getElementById('edit-overlay').style.display = "none";
}
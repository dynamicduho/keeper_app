// deal with opening and closing of the navigation bar links in both index.html
//  and receipts.html
var navLinks= document.getElementById("navLinks");
function showMenu() {
    navLinks.style.right = "0";
}
function hideMenu() {
    navLinks.style.right = "-200px";
}

async function updateReceiptView() {
    try {
        const response = await fetch("http://corn-syrup.csclub.uwaterloo.ca:28401/getReceipts");
        const response_json = await response.json();
        console.log(response_json);
        document.getElementById("receiptView").innerHTML = "";
        if (response_json.length == 0) {
            document.querySelector("#receiptView").innerHTML = "<div class='text-box' style='color: #fff;'>There are currently no receipts uploaded</div>";
        } else {
            for (receipt in response_json) {
                document.querySelector("#receiptView").insertAdjacentHTML('beforeend', `<div class="NFC-col" style="margin-bottom: 15px; margin: 15px;">${response_json[receipt]}</div>`);
            }
        }
    } catch (err) {
        console.log(err);
    }
}

//<form action="http://corn-syrup.csclub.uwaterloo.ca:28401/uploadReceipt" method="post">
async function uploadReceipt() {
    try {
        console.log(document.getElementById('receipt-input').value);
        var search_param = new URLSearchParams();
        search_param.append("receipt", document.getElementById('receipt-input').value);
        // i'm doing these complicated things using search params because i originally take a parameter, receipt, 
        //  in the backend for the post request because it's easy to make in html forms, and i wanna be consistent with what i did before
        await fetch("http://corn-syrup.csclub.uwaterloo.ca:28401/uploadReceipt", {
            //headers: {'Accept': 'application/json', 'Content-Type': 'multipart/form-data', "Access-Control-Allow-Origin":"*"},
            method: "POST",
            body: search_param
        });
        await updateReceiptView();
    } catch (err) {
        console.log(err);
    }
}

// loads receipt view every time the receipts.html loads
window.onload=function() { 
    updateReceiptView();
 };
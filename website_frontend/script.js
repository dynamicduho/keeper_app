// deal with opening and closing of the navigation bar links in both index.html
//  and receipts.html
var navLinks= document.getElementById("navLinks");
function showMenu() {
    navLinks.style.right = "0";
}
function hideMenu() {
    navLinks.style.right = "-200px";
}

function openInput() {
    document.getElementsByClassName('receipt-input2')[0].style.display="inline-block";
    document.getElementsByClassName('receipt_input_closed')[0].style.display ="none";
}
function closeInput() {
    document.getElementsByClassName('receipt-input2')[0].style.display="none";
    document.getElementsByClassName('receipt_input_closed')[0].style.display ="inline-block";
}

function parseReceiptHTML(receipt_str) {
    receipt_json = JSON.parse(receipt_str);
    // console.log(receipt_json)
    items_HTML = receipt_json["items"].map((a) => `<tr><td>${a["item_name"]}</td>
                                                <td>${a["item_quantity"]}</td>
                                                <td>${a["item_price"]}</td></tr>`)
                                                .reduce((a, b) => a + b, "");
    receipt_HTML = `<p>receipt ID: ${receipt_json["receiptID"]}</p>
                    <p>merchant name: ${receipt_json["merchant"]}</p>
                    <p>purchase date: ${receipt_json["date"]}</p>
                    <p>merchant address: ${receipt_json["address"]}</p>
                    <table>
							<tr>
								<th>item name</th>
								<th>item quantity</th>
								<th>item price</th>
							</tr>
							${items_HTML}
                            <tr class="spread"><th>subtotal:</th> <th></th> <th>${receipt_json["item_subtotal"]}</th></tr>
                            <tr class="spread"><th>HST:</th> <th></th> <th>${receipt_json["item_HST"]}</th></tr>
                            <tr class="spread"><th>Total:</th> <th></th> <th>${receipt_json["item_total"]}</th></tr>
					</table>
                    <p>payment method: ${receipt_json["pay_method"]}</p>
    `
    console.log(receipt_HTML)
    return receipt_HTML;
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
                receipt1 = parseReceiptHTML(response_json[receipt]);
                document.querySelector("#receiptView").insertAdjacentHTML('beforeend', `<div class="receipt">${receipt1}</div>`);
            }
        }
    } catch (err) {
        console.log(err);
    }
}

function parseReceiptInput() {
    const items_html = document.getElementById("purchased-items").rows;
    var items_json=[];
    for (item = 1; item < items_html.length; item++) {
        items_json[item - 1]= {// item - 1 to ignore the headers of the table
            "item_name": items_html[item].cells[0].children[0].value,
            "item_quantity": parseInt(items_html[item].cells[1].children[0].value),
            "item_price": Number(items_html[item].cells[2].children[0].value),
        }
    }
    const item_subtotal = items_json.map((a) => a["item_price"] * a["item_quantity"]).reduce((a, b) => a + b, 0);
    console.log(items_json[0]["item_price"])
    receipt_json = {
        "receiptID": document.getElementById('receipt-ID').value,
        "merchant": document.getElementById('merchant').value,
        "date": document.getElementById('purchaseDate').value,
        "address": document.getElementById('address').value,
        "items": items_json,  
        "item_subtotal": item_subtotal,
        "item_HST": Number(document.getElementById('tax').value),
        "item_total": item_subtotal + Number(document.getElementById('tax').value),
        "pay_method": document.getElementById('payment-method').value
    };
    // console.log(receipt_json);
    return receipt_json;
}

async function sendReceipt(receipt_json) {
    try {
        var search_param = new URLSearchParams();
        search_param.append("receipt", receipt_json);
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

//function for submitting input that takes in actual receipt data instead of just a textbox
function uploadReceipt() {
    receipt_json = parseReceiptInput();
    sendReceipt(JSON.stringify(receipt_json));
}

//function for adding new item for receipt input
function addItem() {
    document.querySelector("#purchased-items").insertAdjacentHTML('beforeend', `<tr>
                                                                                    <td><input></td>
                                                                                    <td><input></td>
                                                                                    <td><input></td>
                                                                                </tr>`);
}

// loads receipt view every time the receipts.html loads
window.onload=function() { 
    updateReceiptView();
 };
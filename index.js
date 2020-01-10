var name;
var type;
var accountNum;
var amount;
var transactionId;

var numCompletedUpdates

function completeTransaction() {
	if (document.getElementsByClassName("btn btn-primary btn-lg")[0].innerHTML == "LOADING...") {
		return;
	}
	parseParams()
	var config = {
		projectId: "atm-server-953c5",
		//messagingSenderId: "678522738824"
		apiKey: "AIzaSyCs_6BTTUoPm1zZJeB5-M6PoWozSbdATnA",
		authDomain: "atm-server-953c5.firebaseapp.com",
		databaseURL: "https://atm-server-953c5.firebaseio.com/",
		storageBucket: "atm-server-953c5.appspot.com"
	}
	let fb = firebase.initializeApp(config);

	console.log(fb.database()
		// Get entire Db contents
		.ref())
	//TODO: Remove transaction from db and update balance in db
	let completedUpdates = 0;
	numCompletedUpdates = 0;

	document.getElementsByClassName("btn btn-primary btn-lg")[0].innerHTML = "LOADING...";
	//console.log("HERE")
	// Get current balance and update with transaction amount
	let promise1 = fb.database().ref().once('value').then(snapshot => {
		let entireDbAsJson = snapshot.val();
		let newBalance = entireDbAsJson['Bank Accounts'][Number(accountNum)].Balance;
		let totalBalance = Number(newBalance);
		console.log(type, 'w', type.trim() == 'w')
		type.trim() == 'w' ? totalBalance -= Number(amount) : totalBalance += Number(amount);

		fb.database().ref(`Bank Accounts/${Number(accountNum)}/Balance`).set(totalBalance).then(res => {
			completedUpdates++;
			console.log("Done 1");
			numCompletedUpdates++;
			console.log(numCompletedUpdates);

		});


		return null;
	});

	// Remove transaction from db
	let promise2  = fb.database().ref(`Transactions/${transactionId}`).set(null).then(res => {
		completedUpdates++;
		console.log("Done 2");
		numCompletedUpdates++;
		console.log(numCompletedUpdates)
	});

	//while (numCompletedUpdates < 2) {
	//	//console.log(completedUpdates)
	//}
	Promise.all([promise1, promise2]).then(values => {
		self.close()
	});
	
}


function  parseParams(){

	name = document.getElementById("NamVal").innerHTML;
	type = document.getElementById("TTVal").innerHTML;
	accountNum = document.getElementById("AcVal").innerHTML;
	amount = document.getElementById("AmVal").innerHTML;
	transactionId = document.getElementById("TransVal").innerHTML
}



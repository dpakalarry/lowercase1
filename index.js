var name;
var type;
var accountNum;
var amount;
var transactionId;

function completeTransaction() {
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

	// Get current balance and update with transaction amount
	fb.database().ref().once('value').then(snapshot => {
		let entireDbAsJson = snapshot.val();
		let newBalance = entireDbAsJson['Bank Accounts'][Number(accountNum)].Balance;
		let totalBalance = Number(newBalance);
		console.log(type, 'w', type.trim()=='w')
		type.trim() == 'w' ? totalBalance -= Number(amount) : totalBalance += Number(amount);

		fb.database().ref(`Bank Accounts/${ Number(accountNum) }/Balance`).set(totalBalance);


		return null;
	});

	// Remove transaction from db
	fb.database().ref(`Transactions/${transactionId}`).set(null);
	
	//self.close()
}


function  parseParams(){

	name = document.getElementById("NamVal").innerHTML;
	type = document.getElementById("TTVal").innerHTML;
	accountNum = document.getElementById("AcVal").innerHTML;
	amount = document.getElementById("AmVal").innerHTML;
	transactionId = document.getElementById("TransVal").innerHTML
}



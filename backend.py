 # Name to cross-reference w/ database and ensure security
import pyrebase
import random

config = {
  "apiKey": "AIzaSyCs_6BTTUoPm1zZJeB5-M6PoWozSbdATnA",
  "authDomain": "atm-server-953c5.firebaseapp.com",
  "databaseURL": "https://atm-server-953c5.firebaseio.com/",
  "storageBucket": "atm-server-953c5.appspot.com"
}

firebase = pyrebase.initialize_app(config)

db = firebase.database()

print(db.child("Bank Accounts").get().val())
db.child("Bank Accounts").child("11292906").update({"Balance": 350})
print(db.child("Bank Accounts").get().val())

class BankAccount:
    def __init__(self, id, amt, action):
        self.balance = db.child("Bank Accounts").child("11292906").child("Balance").get().val()
        print(self.balance)
        if action=="deposit":
            self.deposit(amt)
        elif action=="withdraw":
            self.withdraw(amt)
        # name check w/ database

    def deposit(self,amt):
        self.balance = self.balance + amt
        print(self.balance)
        
    def withdraw(self,amt):
        if (self.balance > amt):
            self.balance = self.balance - amt
        else:
            print("You don't have enough money in your account to withdraw " + str(amt));
        print(self.balance)
    
    # def verify(self):


bank_account = BankAccount("11292906", 300, "withdraw")
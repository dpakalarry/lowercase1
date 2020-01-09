import qr
import gui

vs = qr.streamStart()
(data, valid) = qr.readQr(vs)
user = data["user"]
transaction = data["transaction"]

m = gui.Tk()
if valid:
	page = gui.confirmPage(user["name"], transaction["type"], transaction["amount"], user["account"])
	   #name: str
	   #type: str
	   #amount: int
	   #account: int
		#)
	page.genPage(m)
else:
	messagebox.showerror("ERROR", "ERROR: {}".format("INSERT ERROR VAR HERE AS A STRING"))
m.mainloop() 


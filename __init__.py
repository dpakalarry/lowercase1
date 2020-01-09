import qr
import gui

vs = qr.streamStart()
qrFound = False
while not qrFound:
    data = qr.readQr(vs)
    if data != None:
        qrFound = True
qr.quitStream(vs)
user = data["user"]
transaction = data["transaction"]
page = gui.confirmPage(user["name"], transaction["type"], transaction["amount"], user["account"])
m = gui.Tk()
page.genPage(m)

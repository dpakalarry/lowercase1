import qr
import gui
import json
import backend

stream = qr.streamStart()
codeDict = None
while codeDict == None:
    codeDict = qr.readQr(stream)
    if codeDict != None:
        print("returning", codeDict)
        break
qr.quitStream(stream)
qr.cv2.destroyAllWindows()
if codeDict:
    transaction = backend.Transaction(codeDict["acct"], codeDict["amt"], codeDict["type"])
    m = gui.Tk()
    page = gui.confirmPage(transaction.correctName, codeDict["type"], codeDict["amt"], codeDict["acct"])
    page.genPage(m, transaction.execute)
    m.mainloop()
else:
    from tkinter import messagebox
    messagebox.showerror("ERROR", "ERROR: {}".format("Exited"))


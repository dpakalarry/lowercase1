import qr
import gui
import json

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
    page = gui.confirmPage(codeDict["name"], codeDict["type"], codeDict["amount"], codeDict["account"])
    m = gui.Tk()
    page.genPage(m)
    m.mainloop()
else:
	messagebox.showerror("ERROR", "ERROR: {}".format("Exited"))


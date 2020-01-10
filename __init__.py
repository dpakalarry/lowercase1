import qr
import gui
import json
import backend
import webbrowser
from tkinter import messagebox
import os 
import time

stream = qr.streamStart()
codeDict = None
while True:
    codeDict = qr.readQr(stream)
    if codeDict != None:
        try:
            transaction = backend.Transaction(codeDict["acct"], codeDict["amt"], codeDict["type"], codeDict["transId"])
        except:
            messagebox.showerror("ERROR", "ERROR: PLEASE TRY AGAIN")
            codeDict = None
            time.sleep(1)
            continue
        site = open('confirm.html', 'r+')
        html = "".join(site.readlines())
        orig = html
        html = html.replace('NameValue', transaction.correctName)
        html = html.replace('TTValue', codeDict["type"])
        html = html.replace('AccountValue',codeDict["acct"])
        html = html.replace('AmountValue', codeDict["amt"])
        html = html.replace('TransValue', codeDict["transId"])
        print(html)
        site.truncate(0)
        site.write(html)
        site.close()

        filepath = 'file://' + os.path.realpath('confirm.html')
        print(filepath)
        webbrowser.open(filepath)
        codeDict = None
        time.sleep(15)

        site = open('confirm.html', 'w')
        site.write(orig)
        site.close()



            
            
#qr.quitStream(stream)
#qr.cv2.destroyAllWindows()
#if codeDict:
    
#    m = gui.Tk()
#    page = gui.confirmPage(transaction.correctName, codeDict["type"], codeDict["amount"], codeDict["account"])
#    page.genPage(m, transaction.execute)
#    m.mainloop()
#else:
    
#    messagebox.showerror("ERROR", "ERROR: {}".format("Exited"))


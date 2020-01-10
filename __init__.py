import qr
#import gui
import json
import backend
import webbrowser
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
            import ctypes    
            ctypes.windll.user32.MessageBoxW(0, "Error: Invalid Code", "Error", 1)
            codeDict = None
            time.sleep(1)
            continue
        site = open('template.html', 'r')
        html = "".join(site.readlines())
        orig = html
        html = html.replace('NameValue', transaction.correctName)
        html = html.replace('TTValue', codeDict["type"])
        html = html.replace('AccountValue',codeDict["acct"])
        html = html.replace('AmountValue', codeDict["amt"])
        html = html.replace('TransValue', codeDict["transId"])

        site.close()

        nSite = open('confirm.html','w+')
        nSite.truncate(0)
        nSite.write(html)
        nSite.close()


        filepath = 'file://' + os.path.realpath('confirm.html')
        print(filepath)
        webbrowser.open(filepath)
        codeDict = None
        time.sleep(15)

        site = open('confirm.html', 'w')
        site.write(orig)
        site.close()



            
         


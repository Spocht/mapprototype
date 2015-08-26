set shell=WScript.CreateObject("WScript.Shell")
WScript.sleep 50
shell.SendKeys "telnet localhost 5554{ENTER}"
WScript.sleep 2000
shell.SendKeys "geo fix 7.447 46.954{ENTER}"
WScript.sleep 1000
shell.SendKeys "exit{ENTER}{ENTER}"

# qrcodemaker
Generate QR code for clipboard content, EPC data etc

**20-Sep-2022** added a crude GUI for the generation of a mobile payment QR code, ie. the EPC format. As part of this the project was converted
to a Maven project. The maven project builds a 'runnable jar' for the AccountQRMakerGUI application. The original application
which simply displays a QR code for the clipboard content is still present and can be built by running the ant task 'QRCodemaker.xml'

**22-Sep-2022** Added the 'history' to avoid having to reenter previously used account details.

## TODOs
- Make it look nicer
- pretty print account numbers and handle spaces in input
- handle formatted structured communications, ie. containing + and / and space
- remove values from the history
- obfuscate the history file
- display the QR code in the same form as the transaction

Chances are I'll never do any of these as it does what I need it to and probably wont be used that often.
Handling spaces etc. in account and communication are most likely to be actually useful.
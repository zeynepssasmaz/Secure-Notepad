# Secure Notepad

This Java application provides a secure notepad using the DES and AES encryption algorithms. The notepad allows users to open, save, encrypt, and decrypt text files.

## Features

- **File Operations:**
  - **Open:** Load a text file into the notepad.
  - **Save:** Save the contents of the notepad to a text file.

- **Encryption/Decryption:**
  - **Encrypt (DES):** Encrypt the text using the DES algorithm.
  - **Decrypt (DES):** Decrypt the text previously encrypted with DES.
  - **Encrypt (AES):** Encrypt the text using the AES algorithm.
  - **Decrypt (AES):** Decrypt the text previously encrypted with AES.

## Usage

1. **Open/Save File:**
   - Use the "File" menu to open and save text files. The application uses a simple text file format.

2. **Encryption/Decryption:**
   - Use the "Encryption" menu to perform encryption and decryption operations.
   - Select the desired encryption algorithm (DES or AES) and choose the appropriate option.

3. **Viewing Encrypted Text:**
   - Encrypted text is displayed in hexadecimal format.

## How to Build and Run

1. **Clone the repository:**
   ```bash
    git clone https://github.com/zeynepssasmaz/secure-notepad.git
2. **Compile the Java code:**
   ```bash
    javac SecureNotepad.java
3. **Run the application:**
   ```bash
   java SecureNotepad


   

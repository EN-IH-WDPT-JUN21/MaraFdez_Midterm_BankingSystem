# Banking System
Midterm Project / Ironhack Bootcamp 2021
<br/><br/>![banking_system](https://github.com/EN-IH-WDPT-JUN21/MaraFdez_Midterm_BankingSystem/blob/main/img/banking_system.png)

<br/>Welcome.

You are in front of our most recently launched <b>banking system</b>, developed on the special demand of some of the most important banks on a global scale, an application that adapts to everybody needs, simple but robust, intuitive and with no frills.

<br/>This banking system will help you:

-> If you are an <b>admin</b>

· Bring together important information about accounts and its accountholders.

· Check the stored information at any time.

· Store all your banking information.

· Make changes to stored information 

-> If you are an <b>account holder</b>

· Check your accounts 

· Carry out a transaction

-> If you are a <b>third party</b>

· Send or receive money from the account holders

<br/>** This application is equipped with the best fraud detection systems

<br/><br/>
### If you have made it this far, <b>congratulations!</b> 
You have the possibility of carrying out a free trial of the service, your feedback will be of great help to us for future versions.

Do not miss our instructions. In the following, we will show you how to use our system in the most productive way.

<br><br>
## · Getting started

### Run the banking system through an IDE
<br>
1. Download ZIP file of the project
 
![zipdownload](https://github.com/EN-IH-WDPT-JUN21/MaraFdez_Midterm_BankingSystem/blob/main/img/zipdownload.PNG)

2. Extract the ZIP file and open the directory as a project on an IDE such as IntelliJ

3. Set up the application properties :
  _<br/>If you are using it for the first time you should set up `spring.jpa.hibernate.ddl-auto=create`, otherwise you should keep the actual configuration `spring.jpa.hibernate.ddl-auto=update`_
   
4. Set up MySQL :
<br/>_create database banking_system;
<br/>use banking_system;
<br/>CREATE USER 'ironhacker'@'localhost' IDENTIFIED BY '1r0nh4ck3r';
<br/>GRANT ALL PRIVILEGES ON *.* TO 'ironhacker'@'localhost';
<br/>FLUSH PRIVILEGES;_

5. Run the MidtermProjectApplication.java file

6. If you want to populate the database with some sample data, just copy and paste into your MySQL workbench the information that you can find in _resources -> static -> schema.sql_

![dataSQL](https://github.com/EN-IH-WDPT-JUN21/MaraFdez_Midterm_BankingSystem/blob/main/img/dataSQL.PNG)

7. Enjoy

<br><br>
## · How the banking system works
 
With the purpose of offering the best quality application, an updatable database has been created with the following schema

![RelationsDiagram](https://github.com/EN-IH-WDPT-JUN21/MaraFdez_Midterm_BankingSystem/blob/main/img/RelationsDiagram.png)

<br/>The first step, before you can try the different functions that this application offers you, will be to <b>authenticate</b> as an administrator or as an account holder. 

![SigIn](https://github.com/EN-IH-WDPT-JUN21/MaraFdez_Midterm_BankingSystem/blob/main/img/SigIn.PNG)

<br/>**If you have logged as an admin then you can access the following paths, regarding the accounts :**

_http://localhost:8081/account/*_  ->  GET, PATCH, DELETE requests supported 

![accountPatch](https://github.com/EN-IH-WDPT-JUN21/MaraFdez_Midterm_BankingSystem/blob/main/img/accountPatch.PNG)


_http://localhost:8081/checking/*_ ->  GET, POST, DELETE requests supported

![checkingPost](https://github.com/EN-IH-WDPT-JUN21/MaraFdez_Midterm_BankingSystem/blob/main/img/checkingPost.PNG)


_http://localhost:8081/student/*_  ->  GET, DELETE requests supported

![studentDelete](https://github.com/EN-IH-WDPT-JUN21/MaraFdez_Midterm_BankingSystem/blob/main/img/studentDelete.PNG)


_http://localhost:8081/savings/*_  -> GET, POST, DELETE requests supported

![savingsGet](https://github.com/EN-IH-WDPT-JUN21/MaraFdez_Midterm_BankingSystem/blob/main/img/savingsGet.PNG)


_http://localhost:8081/credit/*_   -> GET, POST, DELETE requests supported

![creditGet](https://github.com/EN-IH-WDPT-JUN21/MaraFdez_Midterm_BankingSystem/blob/main/img/creditGet.PNG)


<br/>And the following paths, regarding the users :
<br/><br/>_http://localhost:8081/user/*_     -> GET, DELETE requests supported
<br/>_http://localhost:8081/admin/*_         -> GET requests supported
<br/>_http://localhost:8081/accountHolder/*_ -> GET, POST, DELETE requests supported

![AdminAccountHolder](https://github.com/EN-IH-WDPT-JUN21/MaraFdez_Midterm_BankingSystem/blob/main/img/AdminAccountHolder.PNG)

<br/>_http://localhost:8081/thirdParty/*_    -> GET, POST, DELETE requests supported

![AdminThirdParty](https://github.com/EN-IH-WDPT-JUN21/MaraFdez_Midterm_BankingSystem/blob/main/img/AdminThirdParty.PNG)


<br/>Finally you will be able to access the different transactions made by the users :
<br/><br/>_http://localhost:8081/transaction_ -> GET requests supported

<br/><br/>**If you have logged as an account holder then you can access to your own accounts :**

_http://localhost:8081/myAccount_ -> GET request

![accountHolderMyAccount](https://github.com/EN-IH-WDPT-JUN21/MaraFdez_Midterm_BankingSystem/blob/main/img/accountHolderMyAccount.PNG)

Or carry out transactions between accounts:

_http://localhost:8081/newTransaction_ -> POST request

![newTransaction](https://github.com/EN-IH-WDPT-JUN21/MaraFdez_Midterm_BankingSystem/blob/main/img/newTransaction.PNG)

<br/><br/>**In case you are a third party you do not need to be logged in, you simply have to use your hashed key in the body of the request  :**

_http://localhost:8081//TP/transaction/{hashedKey}_ -> POST request

![TPtransaction](https://github.com/EN-IH-WDPT-JUN21/MaraFdez_Midterm_BankingSystem/blob/main/img/TPtransaction.PNG)

<br/><br/> Finally, we can only wish you a pleasant experience using our system. **Thank you for choosing us as trusted developers.** 


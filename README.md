# SwingInvoice

SwingInvoice helps to model ordering and invoicing in real time. It is built using java swing
and other third party library. It gives the functionality of adding sales order to database.

The orders saved to the database keeps track of
* Sales ID
* Customer Name
* Customer PhoneNumber
* Customer Address
* Item ID
* Item Category
* Item Description
* Item Rate
* Item Quantity
* Item Total Price 


###### NOTE
Each Item Rate ( Low, Medium, High ) have their distinct unit that affects total price calculation

Each Item Category have their various price unit

Item Total Price is calculated on the fly  `Formula = ( cat_price_unit * rate ) * quantity`



## Libraries
miglayout --- (*uses*) helps to structure layout for all swing created components

mysql connector --- (*uses*) helps to connect to database {JDBC}

[itext7](https://developers.itextpdf.com/content/itext-7-jump-start-tutorial/chapter-1-introducing-basic-building-blocks) --- (*uses*) helps to generate pdf

[slf4j-api-1.7.25.jar](https://www.slf4j.org/) --- (*uses*) helps for logging

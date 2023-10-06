trigger calculate on Item__c (before insert, after update, after delete) {
    // Use a map to avoid duplicate entries

    Map<ID, Shipping_invoice__c> updateMap = new Map<ID, Shipping_invoice__c>();
    // creates a new Map object called updateMap with a key of type ID and a value of type Shipping_invoice__c

    // define the subtract integer
    Integer subtract;

    List<Item__c> itemList; // defines a list of type Item__c called itemList
    if (trigger.isInsert || trigger.isUpdate) {
        // this code will only run if the trigger is an insert or update
        itemList = trigger.new; // sets the itemList to the new trigger
        subtract = 1; // sets the subtract integer to 1
    } else if (trigger.isDelete) {
        // this code will only run if the trigger is a delete
        itemList = trigger.old; // sets the itemList to the old trigger
        subtract = -1; // sets the subtract integer to -1
    }

    // Access all the information we need in a single query 
    // rather than querying when we need it.
    // This is a best practice for bulkifying requests

    // create a new set called AllItems
    set<Id> AllItems = new set<id>();
    // The Id data type is a built-in Salesforce data type.

    for (item__c i : itemList) {

        // Assert numbers are not negative.
        System.assert(i.quantity__c > 0, 'Quantity must be positive.');
        System.assert(i.weight__c >= 0, 'Weight must be non-negative.');
        System.assert(i.price__c >= 0, 'Price must be non-negative.');

        // these assertions check custom fields that are defined in the Salesforce
        // setup menu - not in this code.  The __c indicates they are a custom field.
        System.debug('Shipping_invoice__c value: ' + i.Shipping_invoice__c);
        AllItems.add(i.Shipping_invoice__c);
        System.debug('AllItems value: ' + AllItems);

        // we defined Shipping_invoice__c in Salesforce setup menu - not in this code.
    }

    // Access all shipping invoices associated with the items in the trigger via SQL.
    List<Shipping_invoice__c> AllShippingInvoices = [SELECT Id, ShippingDiscount__c,
        Subtotal__c, TotalWeight__c, Tax__c, GrandTotal__c 
        FROM Shipping_invoice__c WHERE Id IN :AllItems];

    Map<ID, Shipping_invoice__c> SIMap = new Map<ID, Shipping_invoice__c>();
    for (Shipping_invoice__c sc: AllShippingInvoices) {
        System.debug('sc: ' + sc);
        SIMap.put(sc.Id, sc); // map the shipping invoice ID to the invoice
        System.debug('SIMap: ' + SIMap);
    }
    System.debug('SIMap: ' + SIMap);

    // Process the list of items on updates
    if (trigger.isUpdate) {
        System.debug('trigger.isUpdate');
        // think of updates as removing the old item and adding the new item
        // rather than changing the existing item's fields.
        // trigger.old == the item pre-update
        // trigger.new == the item post-update
        for (Integer x = 0; x < trigger.old.size(); x++) {
            // define an integer x and set it to 0,
            // then increment it by 1 each time the loop runs.
            // the loop will run as many times as there are items
            // in the trigger.old list.
            
            // define a new invoice called `myOrder`
            Shipping_invoice__c myOrder;
            myOrder = SIMap.get(trigger.old[x].Shipping_invoice__c);

            // Decrement the previous value from the subtotal & weight
            myOrder.Subtotal__c -= (trigger.old[x].price__c * 
                trigger.old[x].quantity__c);
            myOrder.TotalWeight__c -= (trigger.old[x].weight__c *
                trigger.old[x].quantity__c);

            // Now increment the new subtotal & weight
            myOrder.Subtotal__c += (trigger.new[x].price__c *
                trigger.new[x].quantity__c);
            myOrder.TotalWeight__c += (trigger.new[x].weight__c *
                trigger.new[x].quantity__c);
        }

        for (Shipping_invoice__c myOrder : AllShippingInvoices) {
            // for Shipping_invoice__c object in the AllShippingInvoices list
            Decimal taxRate = .0925; // define the tax rate
            myOrder.Tax__c = myOrder.Subtotal__c * taxRate; // calculate the tax

            // reset the shipping discount to 0
            myOrder.ShippingDiscount__c = 0;

            // set the shipping rate to 75 cents per pound
            Decimal shippingRate = .75;

            myOrder.Shipping__c = myOrder.TotalWeight__c * shippingRate;
            myOrder.GrandTotal__c = myOrder.Subtotal__c + myOrder.Tax__c + myOrder.Shipping__c;
            updateMap.put(myOrder.id, myOrder);    
        }
    } else {
        System.debug('This is an insert or delete trigger');
        for (Item__c itemToProcess : itemList) {
            // for each Item__c custom object in the itemList list,
            // refer to that item as itemToProcess
            
            // instantiate a new Shipping_invoice__c object called myOrder
            Shipping_invoice__c myOrder;

            // assign the new myOrder project to the SIMap's record with the same ID
            myOrder = SIMap.get(itemToProcess.Shipping_invoice__c);
            System.debug('myOrder ' + myOrder);
            myOrder.Subtotal__c += (itemToProcess.price__c * 
                itemToProcess.quantity__c * subtract);
            myOrder.TotalWeight__c += (itemToProcess.weight__c *
                itemToProcess.quantity__c * subtract);
            System.debug('myOrder after processing' + myOrder);
        }

        System.debug('AllShippingInvoices ' + AllShippingInvoices);

        for (Shipping_invoice__c myOrder : AllShippingInvoices) {
            // 'for Shipping_invoice__c custom object, which we'll refer to as myOrder,
            // in the AllShippingInvoices list, do the following:'
            System.debug('127 myOrder ' + myOrder);
            // set the tax rate to 9.25%
            Decimal taxRate = .0925;
            myOrder.Tax__c = myOrder.Subtotal__c * taxRate;

            // reset the shipping discount to 0
            myOrder.ShippingDiscount__c = 0;

            // set the shipping rate to 75 cents per pound
            Decimal shippingRate = .75;

            myOrder.Shipping__c = (myOrder.TotalWeight__c * shippingRate);
            myOrder.GrandTotal__c = myOrder.Subtotal__c + myOrder.Tax__c + myOrder.Shipping__c;

            // put the new myOrder object with the updated values in the updateMap
            // that we instantiated earlier in the script

            updateMap.put(myOrder.id, myOrder);
            System.debug('UpdateMap: ' + updateMap);
            System.debug('UpdateMap2: ' + updateMap);
        }
    }

    update updateMap.values();
    // Only use one DML update at the end.
    // TODO: find out - what's a DML update?
}
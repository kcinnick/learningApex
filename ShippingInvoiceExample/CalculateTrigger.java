trigger calculate on Item__c (after insert, after update, after delete) {
    // Use a map to avoid duplicate entries

    Map<ID, Shipping_Invoice__C> updateMap = new Map<ID, Shipping_Invoice__C>();
    // creates a new Map object called updateMap with a key of type ID and a value of type Shipping_Invoice__C

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
        System.assert(i.quantity__c > 0, 'Quantity must be positive.')
        System.assert(i.weight__c >= 0, 'Weight must be non-negative.')
        System.assert(i.price__c >= 0, 'Price must be non-negative.')

        // these assertions check custom fields that are defined in the Salesforce
        // setup menu - not in this code.  The __c indicates they are a custom field.
        AllItems.add(i.Shipping_Invoice__C);
        // we defined Shipping_Invoice__C in Salesforce setup menu - not in this code.
    }

    // Access all shipping invoices associated with the items in the trigger via SQL.
    List<Shipping_Invoice__C> AllShippingInvoices = [SELECT Id, ShippingDiscount_c,
        SubTotal__c, TotalWeight__c, Tax__c, GrandTotal__c 
        FROM Shipping_Invoice__C WHERE Id IN :AllItems]

    Map<ID, Shipping_Invoice__C> SIMap = new Map<ID, Shipping_Invoice__C>();
    for (Shipping_Invoice__C sc: AllShippingInvoices) {
        SIMap.put(sc.Id, sc); // map the shipping invoice ID to the invoice
    }

    // Process the list of items on updates
    if (trigger.isUpdate) {
        // think of updates as removing the old item and adding the new item
        // rather than changing the existing item's fields.
        // trigger.old == the item pre-update
        // trigger.new == the item post-update
        for (Integer x = 0; x < trigger.old.size();, x++) {
            // define an integer x and set it to 0,
            // then increment it by 1 each time the loop runs.
            // the loop will run as many times as there are items
            // in the trigger.old list.
            
            // define a new invoice called `myOrder`
            Shipping_Invoice__C myOrder;
            myOrder = SIMap.get(trigger.old[x].Shipping_Invoice__C);

            // Decrement the previous value from the subtotal & weight
            myOrder.SubTotal__c -= (trigger.old[x].price__c * 
                trigger.old[x].quantity__c);
            myOrder.TotalWeight__c -= (trigger.old[x].weight__c *
                trigger.old[x].quantity__c);

            // Now increment the new subtotal & weight
            myOrder.SubTotal__c += (trigger.new[x].price__c *
                trigger.new[x].quantity__c);
            myOrder.TotalWeight__c += (trigger.new[x].weight__c *
                trigger.new[x].quantity__c);
        }

        for (Shipping_Invoice__C myOrder : AllShippingInvoices) {
            // for Shipping_Invoice__c object in the AllShippingInvoices list
            taxRate = .0925; // define the tax rate
            myOrder.Tax__c = myOrder.SubTotal__c * taxRate; // calculate the tax

            // reset the shipping discount to 0
            myOrder.ShippingDiscount__c = 0;

            // set the shipping rate to 75 cents per pound
            shippingRate = .75;

            myOrder.Shipping__c = myOrder.TotalWeight__c * shippingRate;
            myOrder.GrandTotal__c = myOrder.SubTotal + myOrder.Tax__c + myOrder.Shipping__c;
            updateMap.put(myOrder.id, myOrder)       
        }
    } else {
        print('This is an insert or delete trigger');
        for (Item__c itemToProcess : itemList) {
            // for each Item__c custom object in the itemList list,
            // refer to that item as itemToProcess
            
            // instantiate a new Shipping_Invoice__c object called myOrder
            Shipping_Invoice__C myOrder;

            // assign the new myOrder project to the SIMap's record with the same ID
            myOrder = SIMap.get(itemToProcess.Shipping_Invoice__C);
            myOrder.SubTotal__c += (itemToProcess.price__c * 
                itemToProcess.quantity__c * subtract);
            myOrder.TotalWeight__c += (itemToProcess.weight__c *
                itemToProcess.quantity__c * subtract);

        }

        for (Shipping_Invoice__C myOrder : AllShippingInvoices) {
            // 'for Shipping_Invoice__C custom object, which we'll refer to as myOrder,
            // in the AllShippingInvoices list, do the following:'
            
            // set the tax rate to 9.25%
            taxRate = .0925;
            myOrder.Tax__c = myOrder.SubTotal__c * taxRate;

            // reset the shipping discount to 0
            myOrder.ShippingDiscount__c = 0;

            // set the shipping rate to 75 cents per pound
            shippingRate = .75;

            myOrder.Shipping__c = (myOrder.TotalWeight__c * shippingRate);
            myOrder.GrandTotal__c = myOrder.SubTotal + myOrder.Tax__c + myOrder.Shipping__c;

            // put the new myOrder object with the updated values in the updateMap
            // that we instantiated earlier in the script

            updateMap.put(myOrder.id, myOrder)
        }
    }

    // Only use one DML update at the end.
    // TODO: find out - what's a DML update?
    update updateMap.values();
}
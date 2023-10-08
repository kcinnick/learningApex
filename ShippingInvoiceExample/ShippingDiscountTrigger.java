trigger ShippingDiscount on Shipping_invoice__c (before update) {
    System.debug('Trigger.new size: ' + Trigger.new.size());
    System.debug('Trigger.new: ' + Trigger.new);

    // this trigger will activate when a new Shipping_invoice__c
    // custom object is updated.
    if(Trigger.new.isEmpty()) {
        System.debug('Trigger.new is empty. Exiting trigger.');
        return;
    }

    // apply free shipping on all orders >$100
    // Your trigger logic here
    for (Shipping_invoice__c myShippingInvoice : Trigger.new) {
        // for Shipping_invoice__c, which we'll call myShippingInvoice,
        // in `Trigger.new`, run this code
        System.debug('Processing: ' + myShippingInvoice);
        if(myShippingInvoice == null) {
            System.debug('myShippingInvoice is null. Skipping this iteration.');
            continue;
        } else {
            System.debug('myShippingInvoice is not null: ' + myShippingInvoice);
        }

        if ((myShippingInvoice.Subtotal__c >= 100.00) && 
            (myShippingInvoice.ShippingDiscount__c == 0)) {
                System.debug('myShippingInvoice.Subtotal__c >= 100.00 && myShippingInvoice.ShippingDiscount__c == 0' + myShippingInvoice);
                // if subtotal>100.00 AND a discount hasn't 
                // yet been applied, apply it.
                myShippingInvoice.ShippingDiscount__c = myShippingInvoice.Shipping__c * -1;
                // first we make the amount of shipping negative
                // then we'll add that negative number to the 
                // grand total to cancel out the shipping cost
                System.debug('GrandTotal before discount applied ' + myShippingInvoice.GrandTotal__c);
                myShippingInvoice.GrandTotal__c += myShippingInvoice.ShippingDiscount__c;
                System.debug('GrandTotal after discount applied ' + myShippingInvoice.GrandTotal__c);
                // now the grand total is ex-any shipping costs.
        } else {
                System.debug('myShippingInvoice.Subtotal__c < 100.00 && myShippingInvoice.ShippingDiscount__c == 0' + myShippingInvoice);
        }
    }
}
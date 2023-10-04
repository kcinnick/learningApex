trigger ShippingDiscount on Shipping_Invoice__C (before update) {
    // this trigger will activate when a new Shipping_Invoice__C
    // custom object is updated.

    // apply free shipping on all orders >$100
    for (Shipping_Invoice__C myShippingInvoice : Trigger.new) {
        // for Shipping_Invoice__C, which we'll call myShippingInvoice,
        // in `Trigger.new`, run this code

        if ((myShippingInvoice.subtotal__c >= 100.00) && 
            (myShippingInvoice.ShippingDiscount__c == 0)) {
                // if subtotal>100.00 AND a discount hasn't 
                // yet been applied, apply it.
                myShippingInvoice.ShippingDiscount__c = myShippingInvoice.Shipping__c * -1
                // first we make the amount of shipping negative
                // then we'll add that negative number to the 
                // grand total to cancel out the shipping cost
                myShippingInvoice.GrandTotal__c += myShippingInvoice.ShippingDiscount__c
                // now the grand total is ex-any shipping costs.
        }
    }
}
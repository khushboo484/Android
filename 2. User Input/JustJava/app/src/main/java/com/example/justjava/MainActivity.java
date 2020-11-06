package com.example.justjava;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;

/**
* This app displays an order form to order coffee.
 **/
public class MainActivity extends AppCompatActivity {
    private int quantity = 0;
    private int price = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * This method increases the value of quantity by 1
     */
    public void increment(View view) {
        if(quantity > 50) {
            Toast.makeText(this, "You cannot Order more than 50 cup of coffees!",Toast.LENGTH_SHORT).show();
            return;
        }
        quantity += 1;
        DisplayQuantity(quantity);
    }

    /**
     * This method decreases the value of quantity by 1
     */
    public void decrement(View view) {
        if(quantity < 1) {
            return;
        }
        quantity -= 1;
        DisplayQuantity(quantity);
    }

    /**
     * This method displays the given quantity value on the screen.
     */
    private void DisplayQuantity(int number) {
        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        quantityTextView.setText(""+number);
    }

    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {
        EditText NameOfPerson = (EditText) findViewById(R.id.Name);
        String name = NameOfPerson.getText().toString();

        CheckBox whippedCream = (CheckBox) findViewById(R.id.cream_checkbox);
        boolean isCream = whippedCream.isChecked();

        CheckBox chocolate = (CheckBox) findViewById(R.id.chocolate_checkbox);
        boolean isChoco = chocolate.isChecked();

        price = calculatePrice(isCream, isChoco);
        String msg = createSummaryOrder(isCream, isChoco, name);

        Intent mail = new Intent(Intent.ACTION_SENDTO);
        mail.setData(Uri.parse("mail To:"));
        mail.putExtra(Intent.EXTRA_SUBJECT, "Place Order for "+name);
        mail.putExtra(Intent.EXTRA_TEXT, msg);
        if(mail.resolveActivity(getPackageManager()) != null) {
            startActivity(mail);
        }
        displayMessage(msg);
    }

    /**
     * Calculates the price of the order.
     *
     * quantity is the number of cups of coffee ordered
     */
    private int calculatePrice(boolean Cream, boolean chocolate) {
        int total = price;
        if(Cream) {
            total += 10;
        }
        if(chocolate) {
            total += 20;
        }
        total *= quantity;
        return total;
    }

    /**
     * This method displays the given price on the screen.
     */
//    private void displayPrice(int number) {
//        TextView priceTextView = (TextView) findViewById(R.id.price_text_view);
//        priceTextView.setText(NumberFormat.getCurrencyInstance().format(number));
//    }
    /**
     * This method displays the given text on the screen.
     */
    private void displayMessage(String message) {
        TextView OrderSummaryTextView = (TextView) findViewById(R.id.summary_text_view);
        OrderSummaryTextView.setText(message);
    }

    /**
     * This method returns the summary of whole app
     */
    private String createSummaryOrder(boolean isCream, boolean isChoco, String Name) {
        String pricemsg ="";
        pricemsg +=  "Name: "+Name;
        if(isCream) {
            pricemsg += "\nAdd Whipped Cream topping";
        }
        if(isChoco) {
            pricemsg += "\nAdd Chocolate Topping";
        }
        pricemsg +=  "\nQuantity: " + quantity;
        pricemsg +=  "\nTotal: $"+ price;
        pricemsg +=  "\nThank you !!";
        return pricemsg;
    }
}
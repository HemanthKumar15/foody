package thulasi.hemanthkumar.foody.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import thulasi.hemanthkumar.foody.model.Cart;
import thulasi.hemanthkumar.foody.params.sqlite;


public class CartHandler extends SQLiteOpenHelper {



    public CartHandler (Context context){
        super(context, sqlite.DB_NAME, null, sqlite.DB_VERSION);

    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String create = "CREATE TABLE " + sqlite.TABLE_NAME +"(" +
               sqlite.CART_ID + " STRING PRIMARY KEY, " +
                sqlite.CART_CHILD+" STRING, " +
                sqlite.CART_QTY + " STRING, "+
                sqlite.CART_PRICE + " STRING, " +
                sqlite.CART_TOTAL + " STRING) ";
        Log.i("db", "onCreate: "+create);
        db.execSQL(create);
        Log.d("db","executed");

    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


    }


    public void AddCart(Cart cart, Context context) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(sqlite.CART_ID, cart.getId());
        values.put(sqlite.CART_CHILD, cart.getChild());
        values.put(sqlite.CART_QTY, cart.getQty());
        values.put(sqlite.CART_PRICE, cart.getPrice());
        values.put(sqlite.CART_TOTAL,""+ (Integer.valueOf(cart.getPrice()) * Integer.valueOf(cart.getQty())));

        db.insert(sqlite.TABLE_NAME,null,values);
        Log.d("dbhk", "AddCart: done" + cart.getId().toString());

        db.close();
    }
    public void DropCart(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE "+sqlite.TABLE_NAME);
        Log.d("db","DROPPED: "+sqlite.TABLE_NAME);
    }

    public List<Cart> getCart(){
        List<Cart> cartList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+sqlite.TABLE_NAME+" ;",null);
        if (cursor.moveToFirst()){
            do{
                Cart cart = new Cart();
                cart.setId(cursor.getString(0));
                cart.setChild(cursor.getString(1));
                cart.setQty(cursor.getString(2));
                cart.setPrice(cursor.getString(3));
                cart.setTotal(cursor.getString(4));
                cartList.add(cart);
                Log.d("db", "getCart: "+cart.getId());
            }while (cursor.moveToNext());
        }

        return cartList;
    }

    public int updateCart(Cart cart){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(sqlite.CART_ID,cart.getId());
        values.put(sqlite.CART_CHILD,cart.getChild());
        values.put(sqlite.CART_TOTAL,cart.getTotal());
        values.put(sqlite.CART_QTY,cart.getQty());
        values.put(sqlite.CART_PRICE,cart.getPrice());
        Log.d("db", "updateCart: "+ cart.getId() + cart.getQty());
        return db.update(sqlite.TABLE_NAME, values, sqlite.CART_ID+ "=?",
                new String[]{cart.getId().toString()});
    }

    public void deleteCart(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(sqlite.TABLE_NAME,sqlite.CART_ID + "=?",
                new String[]{String.valueOf(id)});
        db.close();
    }
}

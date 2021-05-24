package com.pickmyorder.asharani;

public class ModelStock {

    String pro_id;
    String reorder_qty;
    String ordered_qty;
    String order_id;

    public static final String TABLE_NAME = "stock_table";
    public static final String COLUMN_PRODCUT_ID = "product_id";
    public static final String COLUMN_QUANTITY = "quantity";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_STOCK_QUANTITY = "stock_quantity";

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_PRODCUT_ID + " TEXT,"
                    + COLUMN_QUANTITY + " TEXT,"
                    + COLUMN_STOCK_QUANTITY + " TEXT"
                    + ")";

    public String getPro_id() {
        return pro_id;
    }

    public void setPro_id(String pro_id) {
        this.pro_id = pro_id;
    }

    public String getReorder_qty() {
        return reorder_qty;
    }

    public void setReorder_qty(String reorder_qty) {
        this.reorder_qty = reorder_qty;
    }

    public String getOrdered_qty() {
        return ordered_qty;
    }

    public void setOrdered_qty(String ordered_qty) {
        this.ordered_qty = ordered_qty;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }



}

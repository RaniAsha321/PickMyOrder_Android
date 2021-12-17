package com.pickmyorder.asharani;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiLogin_Interface {

    String BASE_URL = "https://app.pickmyorder.co.uk/";

    @FormUrlEncoded
    @POST("user_verification")
    Call<ModelLogin> getClient(@Header("Content-Type") String header, @Field("email") String email, @Field("password") String password, @Field("deviceid") String deviceid, @Field("devicestatus") String devicestatus);

    @FormUrlEncoded
    @POST("forgotpassword")
    Call<ModelForgot> Forgot(@Header("Content-Type") String header, @Field("email") String email);

    @FormUrlEncoded
    @POST("getproductcategory")
    Call<ModelProductsCategory> Products(@Field("userid") String userid, @Field("isVan") String isVan) ;

    @FormUrlEncoded
    @POST("getproductsubcategory")
    Call <ModelProductsSubCategory> PRODUCTS_SUB_CATEGORY_CALL(@Field("userid") String userid, @Field("catid") String catid, @Field("isVan") String isVan);

    @FormUrlEncoded
    @POST("getproductsupersubcategory")
    Call<ModelProductsSubSubCategory> PRODUCTS_SUB_SUB_CATEGORY_CALL(@Field("userid") String userid, @Field("subcat") String subcat, @Field("isVan") String isVan);

    @FormUrlEncoded
    @POST("GetProductSuperSubSubCategory")
    Call<ModelSubSubSubCategory> PRODUCTS_SUB_SUB_SUB_CATEGORY_CALL(@Field("userid") String userid, @Field("super_sub_catid") String super_sub_catid, @Field("isVan") String isVan);

    @FormUrlEncoded
    @POST("getproducts")
    Call<ModelDescription> PRODUCT_DESCRIPTION_CALL(@Field("userid") String userid, @Field("supersubcat") String supersubcat, @Field("keystatus") String keystatus, @Field("isVan") String isVan);

    @FormUrlEncoded
    @POST("changepass")
    Call<ModelChangePassword> CHANGE_PASSWORD_CALL(@Header("Content-Type") String header, @Field("userid") String userid, @Field("newpass") String newpass);

    @FormUrlEncoded
    @POST("AppLogout")
    Call<ModelLogout> LOGOUT_CALL(@Header("Content-Type") String header, @Field("userid") String userid, @Field("deviceid") String deviceid, @Field("devicestatus") String devicestatus);

    @FormUrlEncoded
    @POST("NewGetOrderApi")
    Call<ModelBuyNow> CART_CALL(@Field("cart") String cart_string);

    @FormUrlEncoded
    @POST("NewGetVanOrder")
    Call<ModelBuyNow> CART_VAN_CALL(@Field("cart") String cart_string);

    /************************************************** Qoutes API **************************************************************************/

    @FormUrlEncoded
    @POST("GetQuotes")
    Call<ModelGetQuote> SEND_QUOTE(@Field("cart") String cart_string);

    @FormUrlEncoded
    @POST("UserQuotesGroup")
    Call<ModelQuote> GET_QUOTE(@Field("userid") String userid);

    @FormUrlEncoded
    @POST("UserSingleQuotes")
    Call<ModelMyOrder> SINGLE_QUOTE(@Field("ref_id") String refid);

    @FormUrlEncoded
    @POST("MakeAnOrderQuotes")
    Call<ModelBuyQuote> Quote_Order(@Field("Quotes_id") String Quotes_id, @Field("supplier_id") String supplier_id, @Field("delivery_time") String delivery_time, @Field("delivery_Date") String delivery_Date, @Field("collection_date") String collection_date, @Field("collection_time") String collection_time);



    /************************************************** End **************************************************************************/


    @FormUrlEncoded
    @POST("getcatelogues")
    Call<ModelCatalogues> CATALOGUES_CALL(@Field("userid") String userid);

    @FormUrlEncoded
    @POST("newGetCateLouges")
    Call<ModelCatPromo> New_CATALOGUES_CALL(@Field("userid") String userid);

    @FormUrlEncoded
    @POST("Shelves")
    Call<ModelShelves> SHELVES_CALL(@Field("userid") String userid);

    @FormUrlEncoded
    @POST("viewmyorder")
    Call<ModelOrderMenu> ORDER_MENU_CALL(@Field("userid") String userid, @Field("qoutesstatus") String qoutesstatus, @Field("reorder") String reorder);

    @FormUrlEncoded
    @POST("NewSingleOrederApi")
    Call<ModelMyOrder> MY_ORDER_CALL(@Field("ref_id") String refid);


    @FormUrlEncoded
    @POST("GetProjectsApp")
    Call<ModelProjects> PROJECTS_CALL(@Field("userid") String userid);

    /*@FormUrlEncoded
    @POST("GetProjects")
    Call<ModelProjects> PROJECTS_CALL(@Field("userid") String userid);*/

    @FormUrlEncoded
    @POST("SearchKey")
    Call<ModelSearching> SEARCHING_CALL(@Field("SearchKey") String searchkey, @Field("userid") String userid);

    @FormUrlEncoded
    @POST("SearchKey")
    Call<ModelSearching> SEARCHING_CALL_VAN(@Field("SearchKey") String searchkey, @Field("userid") String userid, @Field("isVan") String isVan);

    @FormUrlEncoded
    @POST("GetSupplier")
    Call<ModelSupplier>SUPPLIER_CALL(@Field("userid") String userid);


    /*@FormUrlEncoded
    @POST("sendprojectfromapp")
    Call<ModelAddProject> ADD_PROJECT_CALL(@Field("Project") String Project);
*/
    @FormUrlEncoded
    @POST("AddProjectFromApp")
    Call<ModelAddProject> ADD_PROJECT_CALL(@Field("Project") String Project);

    /*@FormUrlEncoded
    @POST("EditProjectFromApp")
    Call<ModelAddProject> UPDATE_PROJECT_CALL(@Field("Project") String Project);*/

    @FormUrlEncoded
    @POST("EditProjectAndroid")
    Call<ModelAddProject> UPDATE_PROJECT_CALL(@Field("Project") String Project);

    @FormUrlEncoded
    @POST("Getproductvariation")
    Call<ModelVariations> VARIATIONS_CALL(@Field("productid") String productsku);

    @FormUrlEncoded
    @POST("viewAwatingorder")
    Call<ModelAwaiting> AWAITING_CALL(@Field("userid") String userid);

    @FormUrlEncoded
    @POST("ApproveOrderApi")
    Call<Approval> APPROVAL_CALL(@Field("poreff") String poreff, @Field("ponum") String ponum);

    @FormUrlEncoded
    @POST("Moredetails")
    Call<MoreDetails> MORE_DETAILS_CALL(@Field("refid") String refid);

    @FormUrlEncoded
    @POST("SendOprativeEnigineers")
    Call<AssignEngineer> ASSIGN_ENGINEER_CALL(@Field("userid") String userid);

    @FormUrlEncoded
    @POST("GetBillingDetails")
    Call<ModelBillingDetails> BILLING_DETAILS_CALL(@Field("Enginnerid") String Enginnerid);

    @FormUrlEncoded
    @POST("GetDeleveryDetails")
    Call<ModelDefaultDeliveryAddress> DEFAULT_DELIVERY_ADDRESS_CALL(@Field("Enginnerid") String Enginnerid);

    @FormUrlEncoded
    @POST("DeleteProjectFromApp")
    Call<ModelRemoveProject> Remove_Project(@Field("id") String Project_id);

    @FormUrlEncoded
    @POST("SendSellersToApp")
    Call<ModelWholesellers> WHOLESELLERS_CALL(@Field("City") String PostCode);

    @FormUrlEncoded
    @POST("SendCityList")
    Call<ModelCityList> MODEL_CITY_LIST_CALL(@Field("userid") String userid);

    @FormUrlEncoded
    @POST("MakeAPayment")
    Call<ModelPayment> PAYMENT_CALL(@Field("amount") int amount, @Field("token") String token, @Field("currency") String currency, @Field("description") String description, @Field("cart") String cart
            , @Field("card") String card, @Field("exp_month") int exp_month, @Field("exp_year") int exp_year, @Field("brand") String brand, @Field("cvc") String cvc, @Field("savestatus") String savestatus
            , @Field("WholsalerBusinessId") String WholsalerBusinessId);

    @FormUrlEncoded
    @POST("GetCards")
    Call<ModelAddedCards> MODEL_ADDED_CARDS_CALL(@Field("userid") String userid);

    @FormUrlEncoded
    @POST("RemoveCard")
    Call<ModelRemoveCard> REMOVE_CARD_CALL(@Field("id") String userid);

    @FormUrlEncoded
    @POST("AddCard")
    Call<ModelAddCard> ADD_CARD_CALL(@Field("user_id") String userid, @Field("card") String card, @Field("brand") String brand, @Field("exp_month") String exp_month, @Field("exp_year") String exp_year);

    @FormUrlEncoded
    @POST("AddVanReorder")
    Call<StockDetails> STOCK_HISTORY(@Field("order_id") String order_id);

    @FormUrlEncoded
    @POST("VanStockReorder")
    Call<AddVanStock> ADDSTOCK(@Field("ReorderData") String ReorderData);

    @FormUrlEncoded
    @POST("GetSectionByShelve")
    Call<ModelGetSections> GETSHELVES(@Field("Shelveid") String Shelveid);

}

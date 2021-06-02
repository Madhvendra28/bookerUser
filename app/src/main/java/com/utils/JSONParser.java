package com.utils;

import com.model.Address;
import com.model.AddressList;
import com.model.BankDetails;
import com.model.ClaimPostRequirement;
import com.model.Event;
import com.model.EventModelVarient;
import com.model.ModalVariant;
import com.model.ModelPusAddress;
import com.model.ModelPusStore;
import com.model.MoneyRequest;
import com.model.OfferBanner;
import com.model.PayFailData;
import com.model.PojoModel;
import com.model.PojoVariant;
import com.model.PostRequirement;
import com.model.ShipOrderDetails;
import com.model.Site;
import com.model.SiteData;
import com.model.States;
import com.model.TextLink;
import com.model.UserClaim;
import com.model.UserProfile;
import com.model.VariantSite;
import com.model.VariantSiteLink;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by ANKIT on 19-Jan-19.
 */
public class JSONParser {

    public static HashMap<String, ArrayList<String>> parseStatesCityList(JSONArray jsonArray) {
        try {
            if (jsonArray != null && jsonArray.length() > 0) {
                HashMap<String, ArrayList<String>> map = new HashMap<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String state = !jsonObject.isNull(AppURLParams.state) ? jsonObject.getString(AppURLParams.state) : null;
                    try {
                        JSONArray cityJsonArray = !jsonObject.isNull(AppURLParams.city) ? jsonObject.getJSONArray(AppURLParams.city) : null;
                        if (cityJsonArray != null && cityJsonArray.length() > 0) {
                            ArrayList<String> list = new ArrayList<>();
                            for (int j = 0; j < cityJsonArray.length(); j++) {
                                String s = cityJsonArray.getString(j);
                                list.add(s);
                            }

                            map.put(state, list);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                return map;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static List<States> parseStatesCityListModels(JSONArray jsonArray) {
        try {
            if (jsonArray != null && jsonArray.length() > 0) {
                List<States> states = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String state = !jsonObject.isNull(AppURLParams.state) ? jsonObject.getString(AppURLParams.state) : null;
                    try {
                        JSONArray cityJsonArray = !jsonObject.isNull(AppURLParams.city) ? jsonObject.getJSONArray(AppURLParams.city) : null;
                        if (cityJsonArray != null && cityJsonArray.length() > 0) {
                            ArrayList<String> list = new ArrayList<>();
                            for (int j = 0; j < cityJsonArray.length(); j++) {
                                String s = cityJsonArray.getString(j);
                                list.add(s);
                            }
                            states.add(new States(state, list));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                return states;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static UserProfile parseUserProfile(JSONObject jsonObject) {
        try {
            UserProfile userProfile = new UserProfile();
//            userProfile.setUser_id(jsonObject.getString(AppURLParams.dealer_id));
            userProfile.setUser_code(jsonObject.getString(AppURLParams.user_code));
            userProfile.setName(!jsonObject.getString(AppURLParams.name).equals("null") ? jsonObject.getString(AppURLParams.name) : "");
            userProfile.setEmail_id(!jsonObject.isNull(AppURLParams.email_id) ? jsonObject.getString(AppURLParams.email_id) : "");
            userProfile.setContact_no(!jsonObject.getString(AppURLParams.contact_no).equals("null") ? jsonObject.getString(AppURLParams.contact_no) : "");
            userProfile.setWhatsappNumber(!jsonObject.isNull(AppURLParams.whatsapp_no) ? jsonObject.getString(AppURLParams.whatsapp_no) : "");
            userProfile.setGender(!jsonObject.isNull(AppURLParams.gender) ? jsonObject.getString(AppURLParams.gender) : "");
            userProfile.setDate_of_birth(!jsonObject.isNull(AppURLParams.date_of_birth) ? jsonObject.getString(AppURLParams.date_of_birth) : "");

            userProfile.setAddress(!jsonObject.isNull(AppURLParams.address) ? jsonObject.getString(AppURLParams.address) : "");
            userProfile.setArea(!jsonObject.isNull(AppURLParams.area) ? jsonObject.getString(AppURLParams.area) : "");
            userProfile.setState(!jsonObject.isNull(AppURLParams.state) ? jsonObject.getString(AppURLParams.state) : "");
            userProfile.setCity(!jsonObject.isNull(AppURLParams.city) ? jsonObject.getString(AppURLParams.city) : "");
            userProfile.setPostal_code(!jsonObject.isNull(AppURLParams.postal_code) ? jsonObject.getString(AppURLParams.postal_code) : "");
            userProfile.setProfileImage(!jsonObject.isNull(AppURLParams.profile_image) ? jsonObject.getString(AppURLParams.profile_image) : "");

            userProfile.setDocument_type(!jsonObject.isNull(AppURLParams.document_type) ? jsonObject.getString(AppURLParams.document_type) : "");
            userProfile.setDocument_number(!jsonObject.isNull(AppURLParams.document_number) ? jsonObject.getString(AppURLParams.document_number) : "");
            userProfile.setDocumentImage(!jsonObject.isNull(AppURLParams.document_image) ? jsonObject.getString(AppURLParams.document_image) : "");

            userProfile.setFacebook_link(!jsonObject.isNull(AppURLParams.facebook_link) ? jsonObject.getString(AppURLParams.facebook_link) : "");
            userProfile.setTwitter_link(!jsonObject.isNull(AppURLParams.twitter_link) ? jsonObject.getString(AppURLParams.twitter_link) : "");
            userProfile.setInstagram_link(!jsonObject.isNull(AppURLParams.instagram_link) ? jsonObject.getString(AppURLParams.instagram_link) : "");
            userProfile.setTelegram_link(!jsonObject.isNull(AppURLParams.telegram_link) ? jsonObject.getString(AppURLParams.telegram_link) : "");

            userProfile.setVerification_status(!jsonObject.isNull(AppURLParams.verification_status) ? jsonObject.getString(AppURLParams.verification_status) : "");
            userProfile.setStatus(!jsonObject.isNull(AppURLParams.status) ? jsonObject.getString(AppURLParams.status) : "");
            userProfile.setWallet_amount(!jsonObject.isNull(AppURLParams.wallet_amount) ? jsonObject.getString(AppURLParams.wallet_amount) : "");
            userProfile.setToken(!jsonObject.isNull(AppURLParams.token) ? jsonObject.getString(AppURLParams.token) : "");

            return userProfile;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ArrayList<OfferBanner> parseOfferBanner(JSONArray jsonArray) {
        try {
            if (jsonArray != null && jsonArray.length() > 0) {
                ArrayList<OfferBanner> offerBannerArrayList = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    OfferBanner offerBanner = new OfferBanner();
                    offerBanner.setBanner_id(!jsonObject.isNull(AppURLParams.banner_id) ? jsonObject.getString(AppURLParams.banner_id) : "");
                    offerBanner.setImage_path(!jsonObject.isNull(AppURLParams.image_path) ? jsonObject.getString(AppURLParams.image_path) : "");
//                    offerBanner.setUrl(!jsonObject.isNull(AppURLParams.url) ? jsonObject.getString(AppURLParams.url) : "");
                    offerBannerArrayList.add(offerBanner);
                }
                return offerBannerArrayList;
            }

            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ArrayList<Event> parseEventList(JSONArray jsonArray) {
        try {
            if (jsonArray != null && jsonArray.length() > 0) {
                ArrayList<Event> eventArrayList = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Event event = new Event();
                    event.setEvent_id_data(!jsonObject.isNull("event_id_data") ? jsonObject.getString("event_id_data") : "");
                    event.setEvent_id(!jsonObject.isNull(AppURLParams.event_id) ? jsonObject.getString(AppURLParams.event_id) : "");
                    event.setEvent_name(!jsonObject.isNull(AppURLParams.event_name) ? jsonObject.getString(AppURLParams.event_name) : "");
                    event.setSale_type(!jsonObject.isNull(AppURLParams.sale_type) ? jsonObject.getString(AppURLParams.sale_type) : "");
                    event.setStart_date(!jsonObject.isNull(AppURLParams.start_date) ? jsonObject.getString(AppURLParams.start_date) : "");
                    event.setEnd_date(!jsonObject.isNull(AppURLParams.end_date) ? jsonObject.getString(AppURLParams.end_date) : "");
                    event.setShare_date(!jsonObject.isNull(AppURLParams.share_date) ? jsonObject.getString(AppURLParams.share_date) : "");

                    JSONArray sitesJSONArray = !jsonObject.isNull(AppURLParams.site_name) ? jsonObject.getJSONArray(AppURLParams.site_name) : null;
                    if (sitesJSONArray != null) {
                        ArrayList<Site> list = new ArrayList<>();
                        for (int j = 0; j < sitesJSONArray.length(); j++) {
                            JSONObject obj = sitesJSONArray.getJSONObject(j);
                            Site s = new Site();
                            s.setSite_name(obj.getString(AppURLParams.site_name));
                            s.setSite_image(obj.getString(AppURLParams.site_image));
                            list.add(s);
                        }
                        if (list.size() > 0) {
                            event.setSites(list);
                        }
                    }

                    JSONArray modelVariantArray = !jsonObject.isNull(AppURLParams.model_variant) ? jsonObject.getJSONArray(AppURLParams.model_variant) : null;
                    if (modelVariantArray != null) {
                        ArrayList<EventModelVarient> eventModelVarients = new ArrayList<>();
                        for (int j = 0; j < modelVariantArray.length(); j++) {
                            JSONObject modelVariantObject = modelVariantArray.getJSONObject(j);
                            EventModelVarient modelVariant = new EventModelVarient();
                            modelVariant.setModel_name(!modelVariantObject.isNull(AppURLParams.model_name) ? modelVariantObject.getString(AppURLParams.model_name) : "");
                            modelVariant.setModel_note(!modelVariantObject.isNull("model_note") ? modelVariantObject.getString("model_note") : "");
                            JSONArray variantArray = !modelVariantObject.isNull("variant") ? modelVariantObject.getJSONArray("variant") : null;
                            if (variantArray != null) {
                                ArrayList<ModalVariant> modalVariants = new ArrayList<>();
                                for (int k = 0; k < variantArray.length(); k++) {
                                    JSONObject variantObject = variantArray.getJSONObject(k);
                                    ModalVariant variant = new ModalVariant();
                                    variant.setVariant_name(!variantObject.isNull(AppURLParams.variant_name) ? variantObject.getString(AppURLParams.variant_name) : "");
                                    variant.setVariant_price(!variantObject.isNull("price") ? variantObject.getString("price") : "");
                                    JSONArray variantSiteArray = !variantObject.isNull("variant_site") ? variantObject.getJSONArray("variant_site") : null;
                                    ArrayList<VariantSite> variantSites = new ArrayList<>();
                                    if (variantSiteArray != null) {
                                        for (int l = 0; l < variantSiteArray.length(); l++) {
                                            JSONObject variantSiteObject = variantSiteArray.getJSONObject(l);
                                            VariantSite variantSite = new VariantSite();
                                            variantSite.setSite_name(!variantSiteObject.isNull("site_name") ? variantSiteObject.getString("site_name") : "");
                                            ArrayList<VariantSiteLink> variantSiteLinks = new ArrayList<>();
                                            JSONArray variantSiteLinkArray = !variantSiteObject.isNull("link") ? variantSiteObject.getJSONArray("link") : null;
                                            if (variantSiteLinkArray != null) {
                                                for (int m = 0; m < variantSiteLinkArray.length(); m++) {
                                                    JSONObject variantSiteLinkObject = variantSiteLinkArray.getJSONObject(m);
                                                    VariantSiteLink variantSiteLink = new VariantSiteLink();
                                                    variantSiteLink.setModel_color(!variantSiteLinkObject.isNull("model_color") ? variantSiteLinkObject.getString("model_color") : "");
                                                    variantSiteLink.setLink(!variantSiteLinkObject.isNull("link") ? variantSiteLinkObject.getString("link") : "");
                                                    variantSiteLinks.add(variantSiteLink);
                                                }
                                            }
                                            variantSite.setLinks(variantSiteLinks);
                                            variantSites.add(variantSite);
                                        }
                                    }

                                    variant.setVariantSites(variantSites);
                                    modalVariants.add(variant);
                                }
                                modelVariant.setModalVariantArrayList(modalVariants);
                            }

                            eventModelVarients.add(modelVariant);
                        }
                        if (eventModelVarients.size() > 0) {
                            event.setEventModelVarients(eventModelVarients);
                        }
                    }

                    /*JSONArray variantJSONArray2 = !jsonObject.isNull(AppURLParams.model_variant_2) ? jsonObject.getJSONArray(AppURLParams.model_variant_2) : null;
                    if (variantJSONArray2 != null) {
                        ArrayList<ModalVariant> modalVariantArrayList = new ArrayList<>();
                        for (int j = 0; j < variantJSONArray2.length(); j++) {
                            JSONObject variantJsonObject = variantJSONArray2.getJSONObject(j);
                            ModalVariant modalVariant = new ModalVariant();
                            modalVariant.setVariant_name(!variantJsonObject.getString(AppURLParams.variant_name).equals("null") ? variantJsonObject.getString(AppURLParams.variant_name) : "");
                            modalVariant.setVariant_price(!variantJsonObject.getString(AppURLParams.variant_price).equals("null") ? variantJsonObject.getString(AppURLParams.variant_price) : "");
                            modalVariantArrayList.add(modalVariant);
                        }

                        if (modalVariantArrayList.size() > 0) {
                            event.setModalVariantArrayList(modalVariantArrayList);
                        }
                    }*/

                    JSONArray variantColorJSONArray = !jsonObject.isNull(AppURLParams.variant_color) ? jsonObject.getJSONArray(AppURLParams.variant_color) : null;
                    if (variantColorJSONArray != null) {
                        ArrayList<String> list = new ArrayList<>();
                        for (int j = 0; j < variantColorJSONArray.length(); j++) {
                            String s = variantColorJSONArray.getString(j);
                            list.add(s);
                        }
                        if (list.size() > 0) {
                            event.setVariant_color(list);
                        }
                    }

                    event.setIs_offer(!jsonObject.isNull(AppURLParams.is_offer) ? jsonObject.getString(AppURLParams.is_offer) : "");
                    event.setOffer_title(!jsonObject.isNull(AppURLParams.offer_title) ? jsonObject.getString(AppURLParams.offer_title) : "");
                    event.setOffer_details(!jsonObject.isNull(AppURLParams.offer_details) ? jsonObject.getString(AppURLParams.offer_details) : "");
                    event.setEvent_note(!jsonObject.isNull(AppURLParams.note) ? jsonObject.getString(AppURLParams.note) : "");
                    event.setCreate_date(!jsonObject.isNull(AppURLParams.create_date) ? jsonObject.getString(AppURLParams.create_date) : "");
                    event.setUpdate_date(!jsonObject.isNull("update_date") ? jsonObject.getString("update_date") : "");
                    event.setEvent_link(!jsonObject.isNull(AppURLParams.event_link) ? jsonObject.getString(AppURLParams.event_link) : "");
                    JSONArray eventImageArray = !jsonObject.isNull(AppURLParams.event_image) ? jsonObject.getJSONArray(AppURLParams.event_image) : null;
                    if (eventImageArray != null) {
                        ArrayList<String> list = new ArrayList<>();
                        for (int j = 0; j < eventImageArray.length(); j++) {
                            String obj = eventImageArray.getString(j);
                            list.add(obj);
                        }
                        if (list.size() > 0) {
                            event.setEvent_image(list);
                        }
                    }
                    // event.setEvent_image(!jsonObject.isNull(AppURLParams.event_image) ? jsonObject.getString(AppURLParams.event_image) : "");
                    event.setBookkr_charges(!jsonObject.isNull(AppURLParams.bookkr_charges) ? jsonObject.getString(AppURLParams.bookkr_charges) : "");
                    event.setTotal_requirement(!jsonObject.isNull(AppURLParams.total_requirement) ? jsonObject.getString(AppURLParams.total_requirement) : "");
                    eventArrayList.add(event);
                }
                return eventArrayList;
            }

            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Event parseEventPreview(JSONObject jsonObject) {
        try {
            if (jsonObject != null) {
                Event event = new Event();
                event.setEvent_id_data(!jsonObject.isNull("event_id_data") ? jsonObject.getString("event_id_data") : "");
                event.setEvent_id(!jsonObject.isNull(AppURLParams.event_id) ? jsonObject.getString(AppURLParams.event_id) : "");
                event.setEvent_name(!jsonObject.isNull(AppURLParams.event_name) ? jsonObject.getString(AppURLParams.event_name) : "");
                event.setSale_type(!jsonObject.isNull(AppURLParams.sale_type) ? jsonObject.getString(AppURLParams.sale_type) : "");
                event.setStart_date(!jsonObject.isNull(AppURLParams.start_date) ? jsonObject.getString(AppURLParams.start_date) : "");
                event.setEnd_date(!jsonObject.isNull(AppURLParams.end_date) ? jsonObject.getString(AppURLParams.end_date) : "");
                event.setShare_date(!jsonObject.isNull(AppURLParams.share_date) ? jsonObject.getString(AppURLParams.share_date) : "");

                JSONArray sitesJSONArray = !jsonObject.isNull(AppURLParams.site_name) ? jsonObject.getJSONArray(AppURLParams.site_name) : null;
                if (sitesJSONArray != null) {
                    ArrayList<Site> list = new ArrayList<>();
                    for (int j = 0; j < sitesJSONArray.length(); j++) {
                        JSONObject obj = sitesJSONArray.getJSONObject(j);
                        Site s = new Site();
                        s.setSite_name(obj.getString(AppURLParams.site_name));
                        s.setSite_image(obj.getString(AppURLParams.site_image));
                        list.add(s);
                    }
                    if (list.size() > 0) {
                        event.setSites(list);
                    }
                }

                JSONArray modelVariantArray = !jsonObject.isNull(AppURLParams.model_variant) ? jsonObject.getJSONArray(AppURLParams.model_variant) : null;
                if (modelVariantArray != null) {
                    ArrayList<EventModelVarient> eventModelVarients = new ArrayList<>();
                    for (int j = 0; j < modelVariantArray.length(); j++) {
                        JSONObject modelVariantObject = modelVariantArray.getJSONObject(j);
                        EventModelVarient modelVariant = new EventModelVarient();
                        modelVariant.setModel_name(!modelVariantObject.isNull(AppURLParams.model_name) ? modelVariantObject.getString(AppURLParams.model_name) : "");
                        modelVariant.setModel_note(!modelVariantObject.isNull("model_note") ? modelVariantObject.getString("model_note") : "");
                        JSONArray variantArray = !modelVariantObject.isNull("variant") ? modelVariantObject.getJSONArray("variant") : null;
                        if (variantArray != null) {
                            ArrayList<ModalVariant> modalVariants = new ArrayList<>();
                            for (int k = 0; k < variantArray.length(); k++) {
                                JSONObject variantObject = variantArray.getJSONObject(k);
                                ModalVariant variant = new ModalVariant();
                                variant.setVariant_name(!variantObject.isNull(AppURLParams.variant_name) ? variantObject.getString(AppURLParams.variant_name) : "");
                                variant.setVariant_price(!variantObject.isNull("price") ? variantObject.getString("price") : "");
                                JSONArray variantSiteArray = !variantObject.isNull("variant_site") ? variantObject.getJSONArray("variant_site") : null;
                                ArrayList<VariantSite> variantSites = new ArrayList<>();
                                if (variantSiteArray != null) {
                                    for (int l = 0; l < variantSiteArray.length(); l++) {
                                        JSONObject variantSiteObject = variantSiteArray.getJSONObject(l);
                                        VariantSite variantSite = new VariantSite();
                                        variantSite.setSite_name(!variantSiteObject.isNull("site_name") ? variantSiteObject.getString("site_name") : "");
                                        ArrayList<VariantSiteLink> variantSiteLinks = new ArrayList<>();
                                        JSONArray variantSiteLinkArray = !variantSiteObject.isNull("link") ? variantSiteObject.getJSONArray("link") : null;
                                        if (variantSiteLinkArray != null) {
                                            for (int m = 0; m < variantSiteLinkArray.length(); m++) {
                                                JSONObject variantSiteLinkObject = variantSiteLinkArray.getJSONObject(m);
                                                VariantSiteLink variantSiteLink = new VariantSiteLink();
                                                variantSiteLink.setModel_color(!variantSiteLinkObject.isNull("model_color") ? variantSiteLinkObject.getString("model_color") : "");
                                                variantSiteLink.setLink(!variantSiteLinkObject.isNull("link") ? variantSiteLinkObject.getString("link") : "");
                                                variantSiteLinks.add(variantSiteLink);
                                            }
                                        }
                                        variantSite.setLinks(variantSiteLinks);
                                        variantSites.add(variantSite);
                                    }
                                }

                                variant.setVariantSites(variantSites);
                                modalVariants.add(variant);
                            }
                            modelVariant.setModalVariantArrayList(modalVariants);
                        }

                        eventModelVarients.add(modelVariant);
                    }
                    if (eventModelVarients.size() > 0) {
                        event.setEventModelVarients(eventModelVarients);
                    }
                }

                JSONArray variantColorJSONArray = !jsonObject.isNull(AppURLParams.variant_color) ? jsonObject.getJSONArray(AppURLParams.variant_color) : null;
                if (variantColorJSONArray != null) {
                    ArrayList<String> list = new ArrayList<>();
                    for (int j = 0; j < variantColorJSONArray.length(); j++) {
                        String s = variantColorJSONArray.getString(j);
                        list.add(s);
                    }
                    if (list.size() > 0) {
                        event.setVariant_color(list);
                    }
                }

                event.setIs_offer(!jsonObject.isNull(AppURLParams.is_offer) ? jsonObject.getString(AppURLParams.is_offer) : "");
                event.setOffer_title(!jsonObject.isNull(AppURLParams.offer_title) ? jsonObject.getString(AppURLParams.offer_title) : "");
                event.setOffer_details(!jsonObject.isNull(AppURLParams.offer_details) ? jsonObject.getString(AppURLParams.offer_details) : "");
                event.setEvent_note(!jsonObject.isNull(AppURLParams.note) ? jsonObject.getString(AppURLParams.note) : "");
                event.setCreate_date(!jsonObject.isNull(AppURLParams.create_date) ? jsonObject.getString(AppURLParams.create_date) : "");
                event.setUpdate_date(!jsonObject.isNull("update_date") ? jsonObject.getString("update_date") : "");
                event.setEvent_link(!jsonObject.isNull(AppURLParams.event_link) ? jsonObject.getString(AppURLParams.event_link) : "");
                JSONArray eventImageArray = !jsonObject.isNull(AppURLParams.event_image) ? jsonObject.getJSONArray(AppURLParams.event_image) : null;
                if (eventImageArray != null) {
                    ArrayList<String> list = new ArrayList<>();
                    for (int j = 0; j < eventImageArray.length(); j++) {
                        String obj = eventImageArray.getString(j);
                        list.add(obj);
                    }
                    if (list.size() > 0) {
                        event.setEvent_image(list);
                    }
                }
                // event.setEvent_image(!jsonObject.isNull(AppURLParams.event_image) ? jsonObject.getString(AppURLParams.event_image) : "");
                event.setBookkr_charges(!jsonObject.isNull(AppURLParams.bookkr_charges) ? jsonObject.getString(AppURLParams.bookkr_charges) : "");
                event.setTotal_requirement(!jsonObject.isNull(AppURLParams.total_requirement) ? jsonObject.getString(AppURLParams.total_requirement) : "");
                return event;
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static ArrayList<BankDetails> parseBankDetails(JSONArray jsonArray) {
        try {
            if (jsonArray != null && jsonArray.length() > 0) {
                ArrayList<BankDetails> bankDetailsArrayList = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    BankDetails bankDetails = new BankDetails();
//                    bankDetails.setId(!jsonObject.isNull(AppURLParams.id) ? jsonObject.getString(AppURLParams.id) : "");
                    bankDetails.setAccount_type(!jsonObject.isNull(AppURLParams.account_type) ? jsonObject.getString(AppURLParams.account_type) : "");
                    bankDetails.setUpi_id(!jsonObject.isNull(AppURLParams.upi_id) ? jsonObject.getString(AppURLParams.upi_id) : "");
                    bankDetails.setBank_name(!jsonObject.isNull(AppURLParams.bank_name) ? jsonObject.getString(AppURLParams.bank_name) : "");
                    bankDetails.setAccount_number(!jsonObject.isNull(AppURLParams.account_number) ? jsonObject.getString(AppURLParams.account_number) : "");
                    bankDetails.setAccount_name(!jsonObject.isNull(AppURLParams.account_name) ? jsonObject.getString(AppURLParams.account_name) : "");
                    bankDetails.setIfsc_code(!jsonObject.isNull(AppURLParams.ifsc_code) ? jsonObject.getString(AppURLParams.ifsc_code) : "");
                    bankDetails.setBranch_name(!jsonObject.isNull(AppURLParams.branch_name) ? jsonObject.getString(AppURLParams.branch_name) : "");
                    bankDetailsArrayList.add(bankDetails);
                }

                return bankDetailsArrayList;
            }

            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ArrayList<MoneyRequest> parseMoneyRequest(JSONArray jsonArray) {
        try {
            if (jsonArray != null && jsonArray.length() > 0) {
                ArrayList<MoneyRequest> moneyRequestArrayList = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    MoneyRequest moneyRequest = new MoneyRequest();
//                    moneyRequest.setId(!jsonObject.isNull(AppURLParams.id) ? jsonObject.getString(AppURLParams.id) : "");
                    moneyRequest.setCreate_date(!jsonObject.isNull(AppURLParams.create_date) ? jsonObject.getString(AppURLParams.create_date) : "");
                    moneyRequest.setAmount(!jsonObject.isNull(AppURLParams.amount) ? jsonObject.getString(AppURLParams.amount) : "");
                    moneyRequest.setTransaction_id(!jsonObject.isNull(AppURLParams.transaction_id) ? jsonObject.getString(AppURLParams.transaction_id) : "");
                    moneyRequest.setImage(!jsonObject.isNull(AppURLParams.image) ? jsonObject.getString(AppURLParams.image) : "");
                    moneyRequest.setStatus(!jsonObject.isNull(AppURLParams.status) ? jsonObject.getString(AppURLParams.status) : "");
                    moneyRequest.setMessage(!jsonObject.isNull(AppURLParams.message) ? jsonObject.getString(AppURLParams.message) : "");
                    moneyRequestArrayList.add(moneyRequest);
                }

                return moneyRequestArrayList;
            }

            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ArrayList<PostRequirement> parsePostRequirementList(JSONArray jsonArray) {
        try {
            if (jsonArray != null && jsonArray.length() > 0) {
                ArrayList<PostRequirement> postRequirementArrayList = new ArrayList<>();

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    PostRequirement postRequirement = new PostRequirement();
                    postRequirement.setRequirement_id(!jsonObject.getString(AppURLParams.requirement_id).equals("null") ? jsonObject.getString(AppURLParams.requirement_id) : "");
                    postRequirement.setEvent_id(!jsonObject.getString(AppURLParams.event_id).equals("null") ? jsonObject.getString(AppURLParams.event_id) : "");
                    postRequirement.setDealer_id(!jsonObject.getString(AppURLParams.dealer_id).equals("null") ? jsonObject.getString(AppURLParams.dealer_id) : "");
                    postRequirement.setDealer_name(!jsonObject.getString(AppURLParams.dealer_name).equals("null") ? jsonObject.getString(AppURLParams.dealer_name) : "");
                    postRequirement.setDealer_rating(!jsonObject.getString("dealer_rating").equals("null") ? jsonObject.getString("dealer_rating") : "");
                    //postRequirement.setSite_name(!jsonObject.getString(AppURLParams.site_name).equals("null") ? jsonObject.getString(AppURLParams.site_name) : "");
                    JSONArray sitesJSONArray = !jsonObject.isNull(AppURLParams.site_name) ? jsonObject.getJSONArray(AppURLParams.site_name) : null;
                    if (sitesJSONArray != null) {
                        ArrayList<Site> list = new ArrayList<>();
                        for (int j = 0; j < sitesJSONArray.length(); j++) {
                            JSONObject obj = sitesJSONArray.getJSONObject(j);
                            Site s = new Site();
                            s.setSite_name(obj.getString(AppURLParams.site_name));
                            s.setSite_image(obj.getString(AppURLParams.site_image));
                            list.add(s);
                        }
                        if (list.size() > 0) {
                            postRequirement.setSites(list);
                        }
                    }

                    postRequirement.setSale_type(!jsonObject.getString(AppURLParams.sale_type).equals("null") ? jsonObject.getString(AppURLParams.sale_type) : "");
                    postRequirement.setStart_date(!jsonObject.getString(AppURLParams.start_date).equals("null") ? jsonObject.getString(AppURLParams.start_date) : "");
                    postRequirement.setStart_timing(!jsonObject.getString(AppURLParams.start_timing).equals("null") ? jsonObject.getString(AppURLParams.start_timing) : "");
                    postRequirement.setEnd_date(!jsonObject.getString(AppURLParams.end_date).equals("null") ? jsonObject.getString(AppURLParams.end_date) : "");
                    postRequirement.setEnd_timing(!jsonObject.getString(AppURLParams.end_timing).equals("null") ? jsonObject.getString(AppURLParams.end_timing) : "");
                    postRequirement.setModel_name(!jsonObject.getString(AppURLParams.model_name).equals("null") ? jsonObject.getString(AppURLParams.model_name) : "");

                    postRequirement.setRequired_quantity(!jsonObject.getString(AppURLParams.required_quantity).equals("null") ? jsonObject.getString(AppURLParams.required_quantity) : "");
                    postRequirement.setClaim_quantity(!jsonObject.getString(AppURLParams.claim_quantity).equals("null") ? jsonObject.getString(AppURLParams.claim_quantity) : "");
                    postRequirementArrayList.add(postRequirement);
                }

                return postRequirementArrayList;
            } else
                return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ArrayList<ClaimPostRequirement> parseClaimPostRequirementList(JSONArray jsonArray) {
        try {
            if (jsonArray != null && jsonArray.length() > 0) {
                ArrayList<ClaimPostRequirement> claimPostRequirementArrayList = new ArrayList<>();

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    ClaimPostRequirement claimPostRequirement = new ClaimPostRequirement();
                    claimPostRequirement.setRequirement_id(!jsonObject.getString(AppURLParams.requirement_id).equals("null") ? jsonObject.getString(AppURLParams.requirement_id) : "");
                    claimPostRequirement.setClaim_requirement_id(!jsonObject.getString(AppURLParams.claim_requirement_id).equals("null") ? jsonObject.getString(AppURLParams.claim_requirement_id) : "");
                    claimPostRequirement.setCreate_date(!jsonObject.getString(AppURLParams.create_date).equals("null") ? jsonObject.getString(AppURLParams.create_date) : "");
                    claimPostRequirement.setEvent_id(!jsonObject.getString(AppURLParams.event_id).equals("null") ? jsonObject.getString(AppURLParams.event_id) : "");
                    claimPostRequirement.setDealer_id(!jsonObject.getString(AppURLParams.dealer_id).equals("null") ? jsonObject.getString(AppURLParams.dealer_id) : "");
                    claimPostRequirement.setDealer_name(!jsonObject.getString(AppURLParams.dealer_name).equals("null") ? jsonObject.getString(AppURLParams.dealer_name) : "");
                    claimPostRequirement.setEvent_name(!jsonObject.getString(AppURLParams.event_name).equals("null") ? jsonObject.getString(AppURLParams.event_name) : "");
                    claimPostRequirement.setDealer_rating(!jsonObject.getString("dealer_rating").equals("null") ? jsonObject.getString("dealer_rating") : "");
                    //postRequirement.setSite_name(!jsonObject.getString(AppURLParams.site_name).equals("null") ? jsonObject.getString(AppURLParams.site_name) : "");
                    JSONArray sitesJSONArray = !jsonObject.isNull(AppURLParams.site_name) ? jsonObject.getJSONArray(AppURLParams.site_name) : null;
                    if (sitesJSONArray != null) {
                        ArrayList<Site> list = new ArrayList<>();
                        for (int j = 0; j < sitesJSONArray.length(); j++) {
                            JSONObject obj = sitesJSONArray.getJSONObject(j);
                            Site s = new Site();
                            s.setSite_name(obj.getString(AppURLParams.site_name));
                            s.setSite_image(obj.getString(AppURLParams.site_image));
                            list.add(s);
                        }
                        if (list.size() > 0) {
                            claimPostRequirement.setSites(list);
                        }
                    }

                    claimPostRequirement.setSale_type(!jsonObject.getString(AppURLParams.sale_type).equals("null") ? jsonObject.getString(AppURLParams.sale_type) : "");
                    claimPostRequirement.setStart_date(!jsonObject.getString(AppURLParams.start_date).equals("null") ? jsonObject.getString(AppURLParams.start_date) : "");
                    claimPostRequirement.setStart_timing(!jsonObject.getString(AppURLParams.start_timing).equals("null") ? jsonObject.getString(AppURLParams.start_timing) : "");
                    claimPostRequirement.setEnd_date(!jsonObject.getString(AppURLParams.end_date).equals("null") ? jsonObject.getString(AppURLParams.end_date) : "");
                    claimPostRequirement.setEnd_timing(!jsonObject.getString(AppURLParams.end_timing).equals("null") ? jsonObject.getString(AppURLParams.end_timing) : "");
                    claimPostRequirement.setModel_name(!jsonObject.getString(AppURLParams.model_name).equals("null") ? jsonObject.getString(AppURLParams.model_name) : "");

                    claimPostRequirement.setTotal_claim_quantity(!jsonObject.getString("total_claim_quantity").equals("null") ? jsonObject.getString("total_claim_quantity") : "");
                    claimPostRequirement.setConfirm_claim(!jsonObject.getString("confirm_claim").equals("null") ? jsonObject.getString("confirm_claim") : "");
                    claimPostRequirement.setClaim_quantity(!jsonObject.getString("claim_quantity").equals("null") ? jsonObject.getString("claim_quantity") : "");
                    claimPostRequirement.setStatus(!jsonObject.getString("status").equals("null") ? jsonObject.getString("status") : "");
                    claimPostRequirementArrayList.add(claimPostRequirement);
                }

                return claimPostRequirementArrayList;
            } else
                return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static PostRequirement parsePostRequirement(JSONObject jsonObject) {
        try {
            if (jsonObject != null) {
                PostRequirement postRequirement = new PostRequirement();
                postRequirement.setRequirement_id(!jsonObject.getString(AppURLParams.requirement_id).equals("null") ? jsonObject.getString(AppURLParams.requirement_id) : "");
                postRequirement.setEvent_id_data(!jsonObject.isNull("event_id_data") ? jsonObject.getString("event_id_data") : "");
                postRequirement.setEvent_id(!jsonObject.getString(AppURLParams.event_id).equals("null") ? jsonObject.getString(AppURLParams.event_id) : "");
                postRequirement.setDealer_id(!jsonObject.getString(AppURLParams.dealer_id).equals("null") ? jsonObject.getString(AppURLParams.dealer_id) : "");
                postRequirement.setEvent_name(!jsonObject.getString(AppURLParams.event_name).equals("null") ? jsonObject.getString(AppURLParams.event_name) : "");

                JSONArray sitesJSONArray = !jsonObject.isNull(AppURLParams.site_name) ? jsonObject.getJSONArray(AppURLParams.site_name) : null;
                if (sitesJSONArray != null) {
                    ArrayList<Site> list = new ArrayList<>();
                    for (int j = 0; j < sitesJSONArray.length(); j++) {
                        JSONObject obj = sitesJSONArray.getJSONObject(j);
                        Site s = new Site();
                        s.setSite_name(obj.getString(AppURLParams.site_name));
                        s.setSite_image(obj.getString(AppURLParams.site_image));
                        list.add(s);
                    }
                    if (list.size() > 0) {
                        postRequirement.setSites(list);
                    }
                }

                JSONArray allsitesJSONArray = !jsonObject.isNull("all_site_name") ? jsonObject.getJSONArray("all_site_name") : null;
                if (allsitesJSONArray != null) {
                    ArrayList<SiteData> list = new ArrayList<>();
                    for (int j = 0; j < allsitesJSONArray.length(); j++) {
                        JSONObject obj = allsitesJSONArray.getJSONObject(j);
                        SiteData s = new SiteData();
                        s.setSite_name(obj.getString(AppURLParams.site_name));
                        s.setImage(obj.getString(AppURLParams.image));
                        list.add(s);
                    }
                    if (list.size() > 0) {
                        postRequirement.setAllsites(list);
                    }
                }
                postRequirement.setSale_type(!jsonObject.getString(AppURLParams.sale_type).equals("null") ? jsonObject.getString(AppURLParams.sale_type) : "");
                postRequirement.setStart_date(!jsonObject.getString(AppURLParams.start_date).equals("null") ? jsonObject.getString(AppURLParams.start_date) : "");
//                postRequirement.setStart_timing(!jsonObject.getString(AppURLParams.start_timing).equals("null") ? jsonObject.getString(AppURLParams.start_timing) : "");
                postRequirement.setEnd_date(!jsonObject.getString(AppURLParams.end_date).equals("null") ? jsonObject.getString(AppURLParams.end_date) : "");
                //   postRequirement.setEnd_timing(!jsonObject.getString(AppURLParams.end_timing).equals("null") ? jsonObject.getString(AppURLParams.end_timing) : "");

                postRequirement.setRequired_quantity(!jsonObject.getString(AppURLParams.required_quantity).equals("null") ? jsonObject.getString(AppURLParams.required_quantity) : "");
                postRequirement.setClaim_quantity(!jsonObject.getString("claimed_quantity").equals("null") ? jsonObject.getString("claimed_quantity") : "");
                postRequirement.setCan_pay(!jsonObject.getString(AppURLParams.can_pay).equals("null") ? jsonObject.getString(AppURLParams.can_pay) : "");
                postRequirement.setPayment_on(!jsonObject.getString(AppURLParams.payment_on).equals("null") ? jsonObject.getString(AppURLParams.payment_on) : "");
                postRequirement.setRto(!jsonObject.getString(AppURLParams.rto).equals("null") ? jsonObject.getString(AppURLParams.rto) : "0");
                postRequirement.setRto_charges(!jsonObject.getString(AppURLParams.rto_charges).equals("null") ? jsonObject.getString(AppURLParams.rto_charges) : "0");
                postRequirement.setPrecautions(!jsonObject.isNull(AppURLParams.precaution) ? jsonObject.getString(AppURLParams.precaution) : "");
                postRequirement.setNote(!jsonObject.isNull(AppURLParams.dealer_note) ? jsonObject.getString(AppURLParams.dealer_note) : "");
                postRequirement.setShare_data(!jsonObject.isNull("share_data") ? jsonObject.getString("share_data") : "");
                postRequirement.setPosted_date(!jsonObject.isNull("posted_date") ? jsonObject.getString("posted_date") : "");
                postRequirement.setUpdated_date(!jsonObject.isNull("updated_date") ? jsonObject.getString("updated_date") : "");
                postRequirement.setBookkr_charges(!jsonObject.isNull("bookkr_charges") ? jsonObject.getString("bookkr_charges") : "");
                postRequirement.setTotal_bookker_charge(!jsonObject.isNull("total_bookker_charge") ? jsonObject.getString("total_bookker_charge") : "");
                postRequirement.setIs_closed(!jsonObject.isNull("is_closed") ? jsonObject.getString("is_closed") : "");
                postRequirement.setIs_deletable(!jsonObject.isNull("is_deletable") ? jsonObject.getString("is_deletable") : "");

                JSONArray modelVariantJSONArray = !jsonObject.isNull(AppURLParams.model_variant) ? jsonObject.getJSONArray(AppURLParams.model_variant) : null;
                if (modelVariantJSONArray != null) {
                    List<PojoModel> pojoModels = new ArrayList<>();
                    for (int i = 0; i < modelVariantJSONArray.length(); i++) {
                        JSONObject modelVariant = modelVariantJSONArray.getJSONObject(i);
                        PojoModel pojoModel = new PojoModel();
                        pojoModel.setModel_name(modelVariant.getString(AppURLParams.model_name));
                        pojoModel.setModel_id(modelVariant.getString("model_id"));
                        JSONArray colorArray = !modelVariant.isNull("model_color") ? modelVariant.getJSONArray("model_color") : null;
                        if (colorArray != null) {
                            ArrayList<String> modelColors = new ArrayList<>();
                            for (int k = 0; k < colorArray.length(); k++) {
                                modelColors.add(colorArray.getString(k));
                            }
                            //if (modelColors.size() > 0) {
                            pojoModel.setColorList(modelColors);
                            //}
                        }
                        pojoModel.setRequirement_model_id(!modelVariant.isNull("requirement_model_id") ? modelVariant.getString("requirement_model_id") : "");

                        List<PojoVariant> pojoVariants = new ArrayList<>();
                        JSONArray variantsArray = modelVariant.getJSONArray("variant_data");
                        for (int j = 0; j < variantsArray.length(); j++) {
                            JSONObject variant = variantsArray.getJSONObject(j);
                            PojoVariant pojoVariant = new PojoVariant();
                            pojoVariant.setVariant_name(variant.getString(AppURLParams.variant_name));
                            pojoVariant.setVariant_id(variant.getString("variant_id"));
                            pojoVariant.setRequirement_variant_id(variant.getString("requirement_variant_id"));
                            pojoVariant.setVariant_price(variant.getString("variant_price"));
                            pojoVariant.setVariant_color(variant.getString("variant_color"));
                            pojoVariant.setRate_type(variant.getString("rate_type"));
                            pojoVariant.setCod_price(variant.getString("cod_price"));
                            pojoVariant.setPrepaid_price(variant.getString("prepaid_price"));
                            pojoVariant.setPayfail_price(variant.getString("payfail_price"));
                            pojoVariant.setOtp_verify(variant.getString("otp_verify"));

                            JSONArray variantSiteArray = !variant.isNull("variant_site") ? variant.getJSONArray("variant_site") : null;
                            ArrayList<VariantSite> variantSites = new ArrayList<>();
                            if (variantSiteArray != null) {
                                for (int l = 0; l < variantSiteArray.length(); l++) {
                                    JSONObject variantSiteObject = variantSiteArray.getJSONObject(l);
                                    VariantSite variantSite = new VariantSite();
                                    variantSite.setSite_name(!variantSiteObject.isNull("site_name") ? variantSiteObject.getString("site_name") : "");
                                    ArrayList<VariantSiteLink> variantSiteLinks = new ArrayList<>();
                                    JSONArray variantSiteLinkArray = !variantSiteObject.isNull("link") ? variantSiteObject.getJSONArray("link") : null;
                                    if (variantSiteLinkArray != null) {
                                        for (int m = 0; m < variantSiteLinkArray.length(); m++) {
                                            JSONObject variantSiteLinkObject = variantSiteLinkArray.getJSONObject(m);
                                            VariantSiteLink variantSiteLink = new VariantSiteLink();
                                            variantSiteLink.setModel_color(!variantSiteLinkObject.isNull("model_color") ? variantSiteLinkObject.getString("model_color") : "");
                                            variantSiteLink.setLink(!variantSiteLinkObject.isNull("link") ? variantSiteLinkObject.getString("link") : "");
                                            variantSiteLinks.add(variantSiteLink);
                                        }
                                    }
                                    variantSite.setLinks(variantSiteLinks);
                                    variantSites.add(variantSite);
                                }
                            }

                            pojoVariant.setVariantSites(variantSites);
                            pojoVariants.add(pojoVariant);
                        }
                        pojoModel.setVariants(pojoVariants);
                        pojoModels.add(pojoModel);
                    }
                    postRequirement.setModelVariantList(pojoModels);
                }

                JSONObject addressDetailsObject = !jsonObject.isNull("address_details") ? jsonObject.getJSONObject("address_details") : null;
                if (addressDetailsObject != null) {
                    JSONArray normalAddressArray = !addressDetailsObject.isNull("normal_address") ? addressDetailsObject.getJSONArray("normal_address") : null;
                    List<Address> addressArrayList = new ArrayList<>();
                    if (normalAddressArray != null) {
                        for (int j = 0; j < normalAddressArray.length(); j++) {

                            JSONObject addressJsonObject = normalAddressArray.getJSONObject(j);
                            Address address = new Address();
                            address.setAddress_id(!addressJsonObject.isNull(AppURLParams.address_id) ? addressJsonObject.getString(AppURLParams.address_id) : "");
                           /* JSONArray siteListArray = !addressJsonObject.isNull(AppURLParams.site_name) ? addressJsonObject.getJSONArray(AppURLParams.site_name) : new JSONArray();
                            ArrayList<String> site_name = new ArrayList<>();
                            for (int k = 0; k < siteListArray.length(); k++) {
                                site_name.add(siteListArray.getString(k));
                            }*/
                            ArrayList<String> siteNames = new ArrayList<>();
                            JSONArray sitesJSONArray1 = !addressJsonObject.isNull(AppURLParams.site_name) ? addressJsonObject.getJSONArray(AppURLParams.site_name) : null;
                            if (sitesJSONArray1 != null) {
                                ArrayList<Site> list = new ArrayList<>();
                                for (int k = 0; k < sitesJSONArray1.length(); k++) {
                                    JSONObject obj = sitesJSONArray1.getJSONObject(k);
                                    Site s = new Site();
                                    s.setSite_name(obj.getString(AppURLParams.site_name));
                                    s.setSite_image(obj.getString(AppURLParams.site_image));
                                    list.add(s);
                                }
                                if (list.size() > 0) {
                                    for (int i = 0; i < list.size(); i++) {
                                        siteNames.add(list.get(i).getSite_name());
                                    }
                                }
                            }
                            address.setSite_nameList(siteNames);
                            address.setPayment_option(!addressJsonObject.isNull(AppURLParams.payment_option) ? addressJsonObject.getString(AppURLParams.payment_option) : "");
                            address.setName(!addressJsonObject.isNull(AppURLParams.name) ? addressJsonObject.getString(AppURLParams.name) : "");
                            address.setSurname(!addressJsonObject.isNull(AppURLParams.surname) ? addressJsonObject.getString(AppURLParams.surname) : "");
                            address.setName_code(!addressJsonObject.isNull(AppURLParams.name_code) ? addressJsonObject.getString(AppURLParams.name_code) : "");
                            address.setHouse_name(!addressJsonObject.isNull(AppURLParams.house_name) ? addressJsonObject.getString(AppURLParams.house_name) : "");
                            address.setShop_tag(!addressJsonObject.isNull(AppURLParams.shop_tag) ? addressJsonObject.getString(AppURLParams.shop_tag) : "");
                            address.setShop_name(!addressJsonObject.isNull(AppURLParams.shop_name) ? addressJsonObject.getString(AppURLParams.shop_name) : "");
                            address.setShop_name_type(!addressJsonObject.isNull(AppURLParams.shop_name_type) ? addressJsonObject.getString(AppURLParams.shop_name_type) : "");
                            address.setContact_no(!addressJsonObject.isNull(AppURLParams.contact_no) ? addressJsonObject.getString(AppURLParams.contact_no) : "");
                            address.setAddress(!addressJsonObject.isNull(AppURLParams.address) ? addressJsonObject.getString(AppURLParams.address) : "");
                            address.setShop_surname(!addressJsonObject.isNull(AppURLParams.shop_surname) ? addressJsonObject.getString(AppURLParams.shop_surname) : "");
                            address.setShop_tag_name(!addressJsonObject.isNull(AppURLParams.shop_tag_name) ? addressJsonObject.getString(AppURLParams.shop_tag_name) : "");
                            address.setPreposition(!addressJsonObject.isNull(AppURLParams.preposition) ? addressJsonObject.getString(AppURLParams.preposition) : "");
                            ArrayList<AddressList> addressLists = new ArrayList<>();
                            JSONArray addressListArray = !addressJsonObject.isNull(AppURLParams.address_list) ? addressJsonObject.getJSONArray(AppURLParams.address_list) : new JSONArray();
                            for (int k = 0; k < addressListArray.length(); k++) {
                                JSONObject addressObj = addressListArray.getJSONObject(k);
                                AddressList addressList = new AddressList();
                                addressList.setArea(!addressObj.isNull(AppURLParams.area) ? addressObj.getString(AppURLParams.area) : "");
                                addressList.setPostal_code(!addressObj.isNull(AppURLParams.postal_code) ? addressObj.getString(AppURLParams.postal_code) : "");
                                addressList.setColony_name(!addressObj.isNull(AppURLParams.colony_name) ? addressObj.getString(AppURLParams.colony_name) : "");
                                addressList.setLandmark(!addressObj.isNull(AppURLParams.landmark) ? addressObj.getString(AppURLParams.landmark) : "");
                                addressLists.add(addressList);
                            }
                            address.setAddressLists(addressLists);
                            address.setState(!addressJsonObject.isNull(AppURLParams.state) ? addressJsonObject.getString(AppURLParams.state) : "");
                            address.setCity(!addressJsonObject.isNull(AppURLParams.city) ? addressJsonObject.getString(AppURLParams.city) : "");
                            address.setNote(!addressJsonObject.isNull(AppURLParams.note) ? addressJsonObject.getString(AppURLParams.note) : "");
                            address.setLink(!addressJsonObject.isNull(AppURLParams.link) ? addressJsonObject.getString(AppURLParams.link) : "");
                            address.setShare_data(!addressJsonObject.isNull("share_data") ? addressJsonObject.getString("share_data") : "");

                            //address.
                            addressArrayList.add(address);
                        }
                        if (addressArrayList.size() > 0) {
                            postRequirement.setAddressList(addressArrayList);
                        }
                    }

                    JSONArray pusAddressArray = !addressDetailsObject.isNull("pus_address") ? addressDetailsObject.getJSONArray("pus_address") : null;
                    List<ModelPusAddress> pusAddressArrayList = new ArrayList<>();
                    if (pusAddressArray != null) {
                        for (int j = 0; j < pusAddressArray.length(); j++) {
                            JSONObject addressJsonObject = pusAddressArray.getJSONObject(j);
                            Address address = new Address();
                            address.setAddress_id(!addressJsonObject.isNull(AppURLParams.address_id) ? addressJsonObject.getString(AppURLParams.address_id) : "");
                            ModelPusAddress pusAddress = new ModelPusAddress();
                            pusAddress.setAddress_id(!addressJsonObject.isNull(AppURLParams.address_id) ? addressJsonObject.getString(AppURLParams.address_id) : "");
                            /*JSONArray siteListArray = !addressJsonObject.isNull(AppURLParams.site_name) ? addressJsonObject.getJSONArray(AppURLParams.site_name) : new JSONArray();
                            ArrayList<String> site_name = new ArrayList<>();
                            for (int k = 0; k < siteListArray.length(); k++) {
                                site_name.add(siteListArray.getString(k));
                            }*/
                            ArrayList<String> siteNames = new ArrayList<>();
                            JSONArray sitesJSONArray1 = !addressJsonObject.isNull(AppURLParams.site_name) ? addressJsonObject.getJSONArray(AppURLParams.site_name) : null;
                            if (sitesJSONArray1 != null) {
                                ArrayList<Site> list = new ArrayList<>();
                                for (int k = 0; k < sitesJSONArray1.length(); k++) {
                                    JSONObject obj = sitesJSONArray1.getJSONObject(k);
                                    Site s = new Site();
                                    s.setSite_name(obj.getString(AppURLParams.site_name));
                                    s.setSite_image(obj.getString(AppURLParams.site_image));
                                    list.add(s);
                                }
                                if (list.size() > 0) {
                                    for (int i = 0; i < list.size(); i++) {
                                        siteNames.add(list.get(i).getSite_name());
                                    }
                                }
                            }
//                            address.setSite_nameList(siteNames);
                            pusAddress.setSite_nameList(siteNames);
                            pusAddress.setPayment_option(!addressJsonObject.isNull(AppURLParams.payment_option) ? addressJsonObject.getString(AppURLParams.payment_option) : "");
                            pusAddress.setName(!addressJsonObject.isNull(AppURLParams.name) ? addressJsonObject.getString(AppURLParams.name) : "");
                            pusAddress.setSurname(!addressJsonObject.isNull(AppURLParams.surname) ? addressJsonObject.getString(AppURLParams.surname) : "");
                            pusAddress.setName_code(!addressJsonObject.isNull(AppURLParams.name_code) ? addressJsonObject.getString(AppURLParams.name_code) : "");
                            pusAddress.setContact_no(!addressJsonObject.isNull(AppURLParams.contact_no) ? addressJsonObject.getString(AppURLParams.contact_no) : "");
                            ArrayList<ModelPusStore> addressLists = new ArrayList<>();
                            JSONArray addressListArray = !addressJsonObject.isNull(AppURLParams.pus_address) ? addressJsonObject.getJSONArray(AppURLParams.pus_address) : new JSONArray();
                            for (int k = 0; k < addressListArray.length(); k++) {
                                JSONObject addressObj = addressListArray.getJSONObject(k);
                                ModelPusStore addressList = new ModelPusStore();
                                addressList.setPus_address_id(!addressObj.isNull(AppURLParams.pus_address_id) ? addressObj.getString(AppURLParams.pus_address_id) : "");
                                addressList.setStore_name(!addressObj.isNull(AppURLParams.store_name) ? addressObj.getString(AppURLParams.store_name) : "");
                                addressList.setStore_address(!addressObj.isNull(AppURLParams.store_address) ? addressObj.getString(AppURLParams.store_address) : "");
                                addressList.setPostal_code(!addressObj.isNull(AppURLParams.postal_code) ? addressObj.getString(AppURLParams.postal_code) : "");
                                addressList.setStore_image(!addressObj.isNull(AppURLParams.store_image) ? addressObj.getString(AppURLParams.store_image) : "");
                                addressList.setShare_data(!addressObj.isNull(AppURLParams.share_data) ? addressObj.getString(AppURLParams.share_data) : "");
                                addressList.setFull_store_image(!addressObj.isNull(AppURLParams.full_store_image) ? addressObj.getString(AppURLParams.full_store_image) : "");
                                addressLists.add(addressList);
                            }
                            pusAddress.setPus_address(addressLists);
                            pusAddress.setShare_data(!addressJsonObject.isNull("share_data") ? addressJsonObject.getString("share_data") : "");
                            pusAddressArrayList.add(pusAddress);
                        }
                        if (pusAddressArrayList.size() > 0) {
                            postRequirement.setPusAddressList(pusAddressArrayList);
                        }
                    }

                }

                JSONArray textLinkJSONArray = !jsonObject.isNull(AppURLParams.all_links) ? jsonObject.getJSONArray(AppURLParams.all_links) : null;
                if (textLinkJSONArray != null) {
                    ArrayList<TextLink> textLinkArrayList = new ArrayList<>();
                    for (int j = 0; j < textLinkJSONArray.length(); j++) {
                        JSONObject textLinkJsonObject = textLinkJSONArray.getJSONObject(j);
                        TextLink textLink = new TextLink();
                        try {
                            textLink.setRequirement_link_id(!textLinkJsonObject.getString(AppURLParams.requirement_link_id).equals("null") ? textLinkJsonObject.getString(AppURLParams.requirement_link_id) : "");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        textLink.setText(!textLinkJsonObject.getString(AppURLParams.text).equals("null") ? textLinkJsonObject.getString(AppURLParams.text) : "");
                        textLink.setPost_link(!textLinkJsonObject.getString(AppURLParams.post_link).equals("null") ? textLinkJsonObject.getString(AppURLParams.post_link) : "");
                        textLinkArrayList.add(textLink);
                    }
                    if (textLinkArrayList.size() > 0) {
                        postRequirement.setTextLinkArrayList(textLinkArrayList);
                    }
                }
                return postRequirement;
            } else
                return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static HashMap<String, ArrayList<UserClaim>> parseAllUserClaimMap(JSONArray jsonArray) {
        try {
            if (jsonArray != null && jsonArray.length() > 0) {
                HashMap<String, ArrayList<UserClaim>> listHashMap = new HashMap<>();

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    UserClaim userClaim = new UserClaim();
                    JSONObject claimJSONObject = !jsonObject.isNull(AppURLParams.user_claim) ? jsonObject.getJSONObject(AppURLParams.user_claim) : null;
                    if (claimJSONObject != null) {
                        userClaim.setClaim_requirement_id(!claimJSONObject.getString(AppURLParams.claim_requirement_id).equals("null") ? claimJSONObject.getString(AppURLParams.claim_requirement_id) : "");
                        userClaim.setQuantity(!claimJSONObject.getString(AppURLParams.quantity).equals("null") ? claimJSONObject.getString(AppURLParams.quantity) : "0");
                        userClaim.setConfirmed_quantity(!claimJSONObject.getString(AppURLParams.confirmed_quantity).equals("null") ? claimJSONObject.getString(AppURLParams.confirmed_quantity) : "0");
                        userClaim.setFailed_quantity(!claimJSONObject.getString(AppURLParams.fail_quantity).equals("null") ? claimJSONObject.getString(AppURLParams.fail_quantity) : "0");
                        userClaim.setFail_quantity_reason(!claimJSONObject.getString(AppURLParams.fail_quantity_reason).equals("null") ? claimJSONObject.getString(AppURLParams.fail_quantity_reason) : "");
                        userClaim.setPayment_method(!claimJSONObject.getString(AppURLParams.payment_method).equals("null") ? claimJSONObject.getString(AppURLParams.payment_method) : "");
                        userClaim.setIs_pay_for_other(!claimJSONObject.getString(AppURLParams.is_pay_for_other).equals("null") ? claimJSONObject.getString(AppURLParams.is_pay_for_other) : "");
                        userClaim.setPay_amount(!claimJSONObject.getString(AppURLParams.pay_amount).equals("null") ? claimJSONObject.getString(AppURLParams.pay_amount) : "");
                        userClaim.setStatus(!claimJSONObject.getString(AppURLParams.status).equals("null") ? claimJSONObject.getString(AppURLParams.status) : "");

                        userClaim.setRequirement_id(!jsonObject.getString(AppURLParams.requirement_id).equals("null") ? jsonObject.getString(AppURLParams.requirement_id) : "");
                        userClaim.setEvent_id(!jsonObject.getString(AppURLParams.event_id).equals("null") ? jsonObject.getString(AppURLParams.event_id) : "");
                        userClaim.setDealer_id(!jsonObject.getString(AppURLParams.dealer_id).equals("null") ? jsonObject.getString(AppURLParams.dealer_id) : "");
                        userClaim.setDealer_name(!jsonObject.getString(AppURLParams.dealer_name).equals("null") ? jsonObject.getString(AppURLParams.dealer_name) : "");
                        userClaim.setEvent_name(!jsonObject.getString(AppURLParams.event_name).equals("null") ? jsonObject.getString(AppURLParams.event_name) : "");
                        userClaim.setSite_name(!jsonObject.getString(AppURLParams.site_name).equals("null") ? jsonObject.getString(AppURLParams.site_name) : "");
                        userClaim.setSale_type(!jsonObject.getString(AppURLParams.sale_type).equals("null") ? jsonObject.getString(AppURLParams.sale_type) : "");
                        userClaim.setStart_date(!jsonObject.getString(AppURLParams.start_date).equals("null") ? jsonObject.getString(AppURLParams.start_date) : "");
                        userClaim.setStart_timing(!jsonObject.getString(AppURLParams.start_timing).equals("null") ? jsonObject.getString(AppURLParams.start_timing) : "");
                        userClaim.setEnd_date(!jsonObject.getString(AppURLParams.end_date).equals("null") ? jsonObject.getString(AppURLParams.end_date) : "");
                        userClaim.setEnd_timing(!jsonObject.getString(AppURLParams.end_timing).equals("null") ? jsonObject.getString(AppURLParams.end_timing) : "");
                        userClaim.setModel_name(!jsonObject.getString(AppURLParams.model_name).equals("null") ? jsonObject.getString(AppURLParams.model_name) : "");
                        userClaim.setRequired_quantity(!jsonObject.getString(AppURLParams.required_quantity).equals("null") ? jsonObject.getString(AppURLParams.required_quantity) : "");
                        userClaim.setClaim_quantity(!jsonObject.getString(AppURLParams.claim_quantity).equals("null") ? jsonObject.getString(AppURLParams.claim_quantity) : "");
                        userClaim.setCan_pay(!jsonObject.getString(AppURLParams.can_pay).equals("null") ? jsonObject.getString(AppURLParams.can_pay) : "");
                        userClaim.setCan_pay_left(!jsonObject.getString(AppURLParams.can_pay_left).equals("null") ? jsonObject.getString(AppURLParams.can_pay_left) : "");
                        userClaim.setPayment_on(!jsonObject.getString(AppURLParams.payment_on).equals("null") ? jsonObject.getString(AppURLParams.payment_on) : "");
                        userClaim.setRto(!jsonObject.getString(AppURLParams.rto).equals("null") ? jsonObject.getString(AppURLParams.rto) : "0");
                        userClaim.setRto_charges(!jsonObject.getString(AppURLParams.rto_charges).equals("null") ? jsonObject.getString(AppURLParams.rto_charges) : "0");
                        userClaim.setPrecautions(!jsonObject.isNull(AppURLParams.precaution) ? jsonObject.getString(AppURLParams.precaution) : "");
                        userClaim.setNote(!jsonObject.isNull(AppURLParams.dealer_note) ? jsonObject.getString(AppURLParams.dealer_note) : "");
                        userClaim.setIs_live(!jsonObject.getString(AppURLParams.is_live).equals("null") ? jsonObject.getString(AppURLParams.is_live) : "");
                        userClaim.setCreate_date(!jsonObject.getString(AppURLParams.create_date).equals("null") ? jsonObject.getString(AppURLParams.create_date) : "");


                        JSONArray sitesJSONArray = !jsonObject.isNull(AppURLParams.site_variant) ? jsonObject.getJSONArray(AppURLParams.site_variant) : null;
                        if (sitesJSONArray != null) {
                            ArrayList<SiteData> siteDataArrayList = new ArrayList<>();

                            for (int j = 0; j < sitesJSONArray.length(); j++) {
                                JSONObject siteJSONObject = sitesJSONArray.getJSONObject(j);
                                SiteData siteData = new SiteData();
                                siteData.setSite_name(!siteJSONObject.getString(AppURLParams.site_name).equals("null") ? siteJSONObject.getString(AppURLParams.site_name) : "");
                                siteData.setTotal_quantity(!siteJSONObject.getString(AppURLParams.site_quantity).equals("null") ? siteJSONObject.getString(AppURLParams.site_quantity) : "");
                                siteData.setClaim_confirm_id(!siteJSONObject.getString(AppURLParams.claim_confirm_id).equals("null") ? siteJSONObject.getString(AppURLParams.claim_confirm_id) : "");

                                JSONObject payFailJSONObject = !siteJSONObject.isNull(AppURLParams.payfail_details) ? siteJSONObject.getJSONObject(AppURLParams.payfail_details) : null;
                                if (payFailJSONObject != null) {
                                    PayFailData payFailData = new PayFailData();
                                    payFailData.setPayfail_details_id(!payFailJSONObject.getString(AppURLParams.payfail_details_id).equals("null") ? payFailJSONObject.getString(AppURLParams.payfail_details_id) : "");
                                    payFailData.setUsername(!payFailJSONObject.getString(AppURLParams.username).equals("null") ? payFailJSONObject.getString(AppURLParams.username) : "");
                                    payFailData.setPassword(!payFailJSONObject.getString(AppURLParams.password).equals("null") ? payFailJSONObject.getString(AppURLParams.password) : "");
                                    payFailData.setOtp_send_on(!payFailJSONObject.getString(AppURLParams.otp_send_on).equals("null") ? payFailJSONObject.getString(AppURLParams.otp_send_on) : "");
                                    payFailData.setWhatsapp_no(!payFailJSONObject.getString(AppURLParams.whatsapp_no).equals("null") ? payFailJSONObject.getString(AppURLParams.whatsapp_no) : "");
                                    payFailData.setNo_of_orders(!payFailJSONObject.getString(AppURLParams.no_of_orders).equals("null") ? payFailJSONObject.getString(AppURLParams.no_of_orders) : "");
                                    payFailData.setTotal_amount(!payFailJSONObject.getString(AppURLParams.total_amount).equals("null") ? payFailJSONObject.getString(AppURLParams.total_amount) : "");
                                    payFailData.setTime_left(!payFailJSONObject.getString(AppURLParams.time_left).equals("null") ? payFailJSONObject.getString(AppURLParams.time_left) : "");
                                    payFailData.setIs_cod_available(!payFailJSONObject.getString(AppURLParams.is_cod_available).equals("null") ? payFailJSONObject.getString(AppURLParams.is_cod_available) : "1");
                                    siteData.setPayFailData(payFailData);
                                }

                                JSONArray sitesVariantJSONArray = !siteJSONObject.isNull(AppURLParams.variant) ? siteJSONObject.getJSONArray(AppURLParams.variant) : null;
                                if (sitesVariantJSONArray != null) {
                                    ArrayList<ModalVariant> modalVariantArrayList = new ArrayList<>();
                                    for (int k = 0; k < sitesVariantJSONArray.length(); k++) {
                                        JSONObject siteVariantJSONObject = sitesVariantJSONArray.getJSONObject(k);
                                        ModalVariant modalVariant = new ModalVariant();
                                        modalVariant.setRequirement_variant_id(!siteVariantJSONObject.getString(AppURLParams.requirement_variant_id).equals("null") ? siteVariantJSONObject.getString(AppURLParams.requirement_variant_id) : "");
                                        modalVariant.setClaim_quantity_id(!siteVariantJSONObject.getString(AppURLParams.claim_quantity_id).equals("null") ? siteVariantJSONObject.getString(AppURLParams.claim_quantity_id) : "");
                                        modalVariant.setVariant_name(!siteVariantJSONObject.getString(AppURLParams.variant_name).equals("null") ? siteVariantJSONObject.getString(AppURLParams.variant_name) : "");
                                        modalVariant.setVariant_price(!siteVariantJSONObject.getString(AppURLParams.variant_price).equals("null") ? siteVariantJSONObject.getString(AppURLParams.variant_price) : "");
                                        modalVariant.setVariant_color_str(!siteVariantJSONObject.getString(AppURLParams.variant_color).equals("null") ? siteVariantJSONObject.getString(AppURLParams.variant_color) : "");
                                        modalVariant.setRate_type(!siteVariantJSONObject.getString(AppURLParams.rate_type).equals("null") ? siteVariantJSONObject.getString(AppURLParams.rate_type) : "");
                                        modalVariant.setCod_price(!siteVariantJSONObject.getString(AppURLParams.cod_price).equals("null") ? siteVariantJSONObject.getString(AppURLParams.cod_price) : "");
                                        modalVariant.setPrepaid_price(!siteVariantJSONObject.getString(AppURLParams.prepaid_price).equals("null") ? siteVariantJSONObject.getString(AppURLParams.prepaid_price) : "");
                                        modalVariant.setPayfail_price(!siteVariantJSONObject.getString(AppURLParams.payfail_price).equals("null") ? siteVariantJSONObject.getString(AppURLParams.payfail_price) : "");
                                        modalVariant.setOtp_verify(!siteVariantJSONObject.getString(AppURLParams.otp_verify).equals("null") ? siteVariantJSONObject.getString(AppURLParams.otp_verify) : "");

                                        modalVariant.setCod_quantity(!siteVariantJSONObject.getString(AppURLParams.cod_quantity).equals("null") ? siteVariantJSONObject.getString(AppURLParams.cod_quantity) : "");
                                        modalVariant.setPrepaid_quantity(!siteVariantJSONObject.getString(AppURLParams.prepaid_quantity).equals("null") ? siteVariantJSONObject.getString(AppURLParams.prepaid_quantity) : "");
                                        modalVariant.setPayfail_quantity(!siteVariantJSONObject.getString(AppURLParams.payfail_quantity).equals("null") ? siteVariantJSONObject.getString(AppURLParams.payfail_quantity) : "");
                                        modalVariant.setOtp_quantity(!siteVariantJSONObject.getString(AppURLParams.otp_quantity).equals("null") ? siteVariantJSONObject.getString(AppURLParams.otp_quantity) : "");
                                        modalVariant.setShipped_cod_quantity(!siteVariantJSONObject.getString(AppURLParams.shipped_cod_quantity).equals("null") ? siteVariantJSONObject.getString(AppURLParams.shipped_cod_quantity) : "");
                                        modalVariant.setShipped_prepaid_quantity(!siteVariantJSONObject.getString(AppURLParams.shipped_prepaid_quantity).equals("null") ? siteVariantJSONObject.getString(AppURLParams.shipped_prepaid_quantity) : "");
                                        modalVariant.setShipped_payfail_quantity(!siteVariantJSONObject.getString(AppURLParams.shipped_payfail_quantity).equals("null") ? siteVariantJSONObject.getString(AppURLParams.shipped_payfail_quantity) : "");
                                        modalVariant.setShipped_otp_verify_quantity(!siteVariantJSONObject.getString(AppURLParams.shipped_otp_verify_quantity).equals("null") ? siteVariantJSONObject.getString(AppURLParams.shipped_otp_verify_quantity) : "");

                                        modalVariantArrayList.add(modalVariant);
                                    }

                                    if (modalVariantArrayList.size() > 0) {
                                        siteData.setModalVariantArrayList(modalVariantArrayList);
                                    }
                                }

                                siteDataArrayList.add(siteData);
                            }

                            if (siteDataArrayList.size() > 0) {
                                userClaim.setSiteDataArrayList(siteDataArrayList);
                            }
                        }


                        JSONArray variantJSONArray = !jsonObject.isNull(AppURLParams.variant) ? jsonObject.getJSONArray(AppURLParams.variant) : null;
                        if (variantJSONArray != null) {
                            ArrayList<ModalVariant> modalVariantArrayList = new ArrayList<>();
                            for (int j = 0; j < variantJSONArray.length(); j++) {
                                JSONObject variantJsonObject = variantJSONArray.getJSONObject(j);
                                ModalVariant modalVariant = new ModalVariant();
                                modalVariant.setRequirement_variant_id(!variantJsonObject.getString(AppURLParams.requirement_variant_id).equals("null") ? variantJsonObject.getString(AppURLParams.requirement_variant_id) : "");
                                modalVariant.setVariant_name(!variantJsonObject.getString(AppURLParams.variant_name).equals("null") ? variantJsonObject.getString(AppURLParams.variant_name) : "");
                                modalVariant.setVariant_price(!variantJsonObject.getString(AppURLParams.variant_price).equals("null") ? variantJsonObject.getString(AppURLParams.variant_price) : "");
                                modalVariant.setVariant_color_str(!variantJsonObject.getString(AppURLParams.variant_color).equals("null") ? variantJsonObject.getString(AppURLParams.variant_color) : "");

                                /*JSONArray colorJSONArray = !variantJsonObject.isNull(AppURLParams.variant_color) ? variantJsonObject.getJSONArray(AppURLParams.variant_color) : null;
                                if (colorJSONArray != null) {
                                    ArrayList<String> list = new ArrayList<>();
                                    for (int a = 0; a < colorJSONArray.length(); a++) {
                                        String s = colorJSONArray.getString(a);
                                        list.add(s);
                                    }
                                    if (list.size() > 0) {
                                        modalVariant.setVariant_color(list);
                                    }
                                }*/

                                modalVariant.setRate_type(!variantJsonObject.getString(AppURLParams.rate_type).equals("null") ? variantJsonObject.getString(AppURLParams.rate_type) : "");
                                modalVariant.setCod_price(!variantJsonObject.getString(AppURLParams.cod_price).equals("null") ? variantJsonObject.getString(AppURLParams.cod_price) : "");
                                modalVariant.setPrepaid_price(!variantJsonObject.getString(AppURLParams.prepaid_price).equals("null") ? variantJsonObject.getString(AppURLParams.prepaid_price) : "");
                                modalVariant.setPayfail_price(!variantJsonObject.getString(AppURLParams.payfail_price).equals("null") ? variantJsonObject.getString(AppURLParams.payfail_price) : "");
                                modalVariant.setOtp_verify(!variantJsonObject.getString(AppURLParams.otp_verify).equals("null") ? variantJsonObject.getString(AppURLParams.otp_verify) : "");
                                modalVariantArrayList.add(modalVariant);
                            }
                            if (modalVariantArrayList.size() > 0) {
                                userClaim.setModalVariantArrayList(modalVariantArrayList);
                            }
                        }

                        /*JSONArray addressJSONArray = !jsonObject.isNull(AppURLParams.address_details) ? jsonObject.getJSONArray(AppURLParams.address_details) : null;
                        if (addressJSONArray != null) {
                            ArrayList<Address> addressArrayList = new ArrayList<>();
                            for (int j = 0; j < addressJSONArray.length(); j++) {

                                JSONObject addressJsonObject = addressJSONArray.getJSONObject(j);
                                Address address = new Address();
                                address.setAddress_id(!addressJsonObject.isNull(AppURLParams.address_id) ? addressJsonObject.getString(AppURLParams.address_id) : "");
                                address.setSite_name(!addressJsonObject.isNull(AppURLParams.site_name) ? addressJsonObject.getString(AppURLParams.site_name) : "");
                                address.setPayment_option(!addressJsonObject.isNull(AppURLParams.payment_option) ? addressJsonObject.getString(AppURLParams.payment_option) : "");
                                address.setName(!addressJsonObject.isNull(AppURLParams.name) ? addressJsonObject.getString(AppURLParams.name) : "");
                                address.setSurname(!addressJsonObject.isNull(AppURLParams.surname) ? addressJsonObject.getString(AppURLParams.surname) : "");
                                address.setName_code(!addressJsonObject.isNull(AppURLParams.name_code) ? addressJsonObject.getString(AppURLParams.name_code) : "");
                                address.setHouse_name(!addressJsonObject.isNull(AppURLParams.house_name) ? addressJsonObject.getString(AppURLParams.house_name) : "");
                                address.setShop_tag(!addressJsonObject.isNull(AppURLParams.shop_tag) ? addressJsonObject.getString(AppURLParams.shop_tag) : "");
                                address.setShop_name(!addressJsonObject.isNull(AppURLParams.shop_name) ? addressJsonObject.getString(AppURLParams.shop_name) : "");
                                address.setShop_type(!addressJsonObject.isNull(AppURLParams.shop_type) ? addressJsonObject.getString(AppURLParams.shop_type) : "");
                                address.setContact_no(!addressJsonObject.isNull(AppURLParams.contact_no) ? addressJsonObject.getString(AppURLParams.contact_no) : "");
                                address.setAddress(!addressJsonObject.isNull(AppURLParams.address) ? addressJsonObject.getString(AppURLParams.address) : "");
                                address.setArea(!addressJsonObject.isNull(AppURLParams.area) ? addressJsonObject.getString(AppURLParams.area) : "");
                                address.setPostal_code(!addressJsonObject.isNull(AppURLParams.postal_code) ? addressJsonObject.getString(AppURLParams.postal_code) : "");
                                address.setColony_name(!addressJsonObject.isNull(AppURLParams.colony_name) ? addressJsonObject.getString(AppURLParams.colony_name) : "");
                                address.setLandmark(!addressJsonObject.isNull(AppURLParams.landmark) ? addressJsonObject.getString(AppURLParams.landmark) : "");
                                address.setState(!addressJsonObject.isNull(AppURLParams.state) ? addressJsonObject.getString(AppURLParams.state) : "");
                                address.setCity(!addressJsonObject.isNull(AppURLParams.city) ? addressJsonObject.getString(AppURLParams.city) : "");
                                address.setNote(!addressJsonObject.isNull(AppURLParams.note) ? addressJsonObject.getString(AppURLParams.note) : "");
                                address.setLink(!addressJsonObject.isNull(AppURLParams.link) ? addressJsonObject.getString(AppURLParams.link) : "");
                                addressArrayList.add(address);
                            }
                            if (addressArrayList.size() > 0) {
                                userClaim.setAddressArrayList(addressArrayList);
                            }
                        }
*/
                        JSONArray textLinkJSONArray = !jsonObject.isNull(AppURLParams.all_links) ? jsonObject.getJSONArray(AppURLParams.all_links) : null;
                        if (textLinkJSONArray != null) {
                            ArrayList<TextLink> textLinkArrayList = new ArrayList<>();
                            for (int j = 0; j < textLinkJSONArray.length(); j++) {
                                JSONObject textLinkJsonObject = textLinkJSONArray.getJSONObject(j);
                                TextLink textLink = new TextLink();
                                textLink.setRequirement_link_id(!textLinkJsonObject.getString(AppURLParams.requirement_link_id).equals("null") ? textLinkJsonObject.getString(AppURLParams.requirement_link_id) : "");
                                textLink.setText(!textLinkJsonObject.getString(AppURLParams.text).equals("null") ? textLinkJsonObject.getString(AppURLParams.text) : "");
                                textLink.setPost_link(!textLinkJsonObject.getString(AppURLParams.post_link).equals("null") ? textLinkJsonObject.getString(AppURLParams.post_link) : "");
                                textLinkArrayList.add(textLink);
                            }
                            if (textLinkArrayList.size() > 0) {
                                userClaim.setTextLinkArrayList(textLinkArrayList);
                            }
                        }

                    }

                    ArrayList<UserClaim> list = listHashMap.get(userClaim.getIs_live());
                    if (list == null) {
                        list = new ArrayList<>();
                    }
                    list.add(userClaim);
                    listHashMap.put(userClaim.getIs_live(), list);
                }

                return listHashMap;
            } else
                return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static HashMap<String, ArrayList<ShipOrderDetails>> parseAllShipOrderDetailsMap(JSONArray jsonArray) {
        try {
            if (jsonArray != null && jsonArray.length() > 0) {
                HashMap<String, ArrayList<ShipOrderDetails>> listHashMap = new HashMap<>();

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject shipJSONObject = jsonArray.getJSONObject(i);
                    ShipOrderDetails shipOrderDetails = new ShipOrderDetails();
                    shipOrderDetails.setShipping_details_id(!shipJSONObject.getString(AppURLParams.shipping_details_id).equals("null") ? shipJSONObject.getString(AppURLParams.shipping_details_id) : "");
                    shipOrderDetails.setCreate_date(!shipJSONObject.getString(AppURLParams.create_date).equals("null") ? shipJSONObject.getString(AppURLParams.create_date) : "");
                    shipOrderDetails.setDealer_name(!shipJSONObject.getString(AppURLParams.dealer_name).equals("null") ? shipJSONObject.getString(AppURLParams.dealer_name) : "");
                    shipOrderDetails.setRequirement_id(!shipJSONObject.getString(AppURLParams.requirement_id).equals("null") ? shipJSONObject.getString(AppURLParams.requirement_id) : "");
                    shipOrderDetails.setClaim_confirm_id(!shipJSONObject.getString(AppURLParams.claim_confirm_id).equals("null") ? shipJSONObject.getString(AppURLParams.claim_confirm_id) : "");
                    shipOrderDetails.setExpected_date(!shipJSONObject.getString(AppURLParams.expected_date).equals("null") ? shipJSONObject.getString(AppURLParams.expected_date) : "");
                    shipOrderDetails.setSite_name(!shipJSONObject.getString(AppURLParams.site_name).equals("null") ? shipJSONObject.getString(AppURLParams.site_name) : "");

                    shipOrderDetails.setName_on_order(!shipJSONObject.getString(AppURLParams.name_on_order).equals("null") ? shipJSONObject.getString(AppURLParams.name_on_order) : "");
                    shipOrderDetails.setModel(!shipJSONObject.getString(AppURLParams.model).equals("null") ? shipJSONObject.getString(AppURLParams.model) : "");
                    shipOrderDetails.setPayment_mode(!shipJSONObject.getString(AppURLParams.payment_mode).equals("null") ? shipJSONObject.getString(AppURLParams.payment_mode) : "");
                    shipOrderDetails.setOrder_value(!shipJSONObject.getString(AppURLParams.order_value).equals("null") ? shipJSONObject.getString(AppURLParams.order_value) : "");
                    shipOrderDetails.setTracking_id(!shipJSONObject.getString(AppURLParams.tracking_id).equals("null") ? shipJSONObject.getString(AppURLParams.tracking_id) : "");
                    shipOrderDetails.setCourier(!shipJSONObject.getString(AppURLParams.courier).equals("null") ? shipJSONObject.getString(AppURLParams.courier) : "");
                    shipOrderDetails.setTracking_link(!shipJSONObject.getString(AppURLParams.tracking_link).equals("null") ? shipJSONObject.getString(AppURLParams.tracking_link) : "");
                    shipOrderDetails.setAddress(!shipJSONObject.getString(AppURLParams.address).equals("null") ? shipJSONObject.getString(AppURLParams.address) : "");
                    shipOrderDetails.setAddress_image(!shipJSONObject.getString(AppURLParams.address_image).equals("null") ? shipJSONObject.getString(AppURLParams.address_image) : "");
                    shipOrderDetails.setSecret_note(!shipJSONObject.getString(AppURLParams.secret_note).equals("null") ? shipJSONObject.getString(AppURLParams.secret_note) : "");
                    shipOrderDetails.setStatus(!shipJSONObject.getString(AppURLParams.status).equals("null") ? shipJSONObject.getString(AppURLParams.status) : "");

                    shipOrderDetails.setCourier_boy_no(!shipJSONObject.getString(AppURLParams.courier_boy_no).equals("null") ? shipJSONObject.getString(AppURLParams.courier_boy_no) : "");
                    shipOrderDetails.setPin(!shipJSONObject.getString(AppURLParams.pin).equals("null") ? shipJSONObject.getString(AppURLParams.pin) : "");
                    shipOrderDetails.setOtp_for_delivery(!shipJSONObject.getString(AppURLParams.otp_for_delivery).equals("null") ? shipJSONObject.getString(AppURLParams.otp_for_delivery) : "");
                    shipOrderDetails.setComment(!shipJSONObject.getString(AppURLParams.comment).equals("null") ? shipJSONObject.getString(AppURLParams.comment) : "");
                    shipOrderDetails.setIs_online_pay(!shipJSONObject.getString(AppURLParams.is_online_pay).equals("null") ? shipJSONObject.getString(AppURLParams.is_online_pay) : "");

                    JSONArray sitesVariantJSONArray = !shipJSONObject.isNull(AppURLParams.variant) ? shipJSONObject.getJSONArray(AppURLParams.variant) : null;
                    if (sitesVariantJSONArray != null) {
                        ArrayList<ModalVariant> modalVariantArrayList = new ArrayList<>();
                        for (int k = 0; k < sitesVariantJSONArray.length(); k++) {
                            JSONObject siteVariantJSONObject = sitesVariantJSONArray.getJSONObject(k);
                            ModalVariant modalVariant = new ModalVariant();
                            modalVariant.setShipped_variant_id(!siteVariantJSONObject.getString(AppURLParams.shipped_variant_id).equals("null") ? siteVariantJSONObject.getString(AppURLParams.shipped_variant_id) : "");
                            modalVariant.setVariant_name(!siteVariantJSONObject.getString(AppURLParams.variant_name).equals("null") ? siteVariantJSONObject.getString(AppURLParams.variant_name) : "");
                            modalVariant.setVariant_price(!siteVariantJSONObject.getString(AppURLParams.variant_price).equals("null") ? siteVariantJSONObject.getString(AppURLParams.variant_price) : "");
                            modalVariant.setSelected_color(!siteVariantJSONObject.getString(AppURLParams.color).equals("null") ? siteVariantJSONObject.getString(AppURLParams.color) : "");
                            modalVariant.setShipping_quantity(!siteVariantJSONObject.getString(AppURLParams.quantity).equals("null") ? siteVariantJSONObject.getString(AppURLParams.quantity) : "");
                            modalVariant.setAdvance_paid(!siteVariantJSONObject.getString(AppURLParams.advance_pay).equals("null") ? siteVariantJSONObject.getString(AppURLParams.advance_pay) : "");

                            modalVariantArrayList.add(modalVariant);
                        }

                        if (modalVariantArrayList.size() > 0) {
                            shipOrderDetails.setModalVariantArrayList(modalVariantArrayList);
                        }
                    }

                    ArrayList<ShipOrderDetails> list = listHashMap.get(shipOrderDetails.getStatus());
                    if (list == null) {
                        list = new ArrayList<>();
                    }
                    list.add(shipOrderDetails);
                    listHashMap.put(shipOrderDetails.getStatus(), list);
                }

                return listHashMap;
            } else
                return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

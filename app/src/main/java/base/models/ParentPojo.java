package base.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ParentPojo {
    private String account = "Account";
    private String accountDetails = "Account Details";
    private String contactAppName = "Contact app name for account details";
    private String enterAccountId = "Enter Account Id";
    private String enterLoginId = "Enter Login Id";
    private String password = "Password";
    private String addAccount = "Add Account";
    private String removeAccount = "Remove";

    public String getAccountDetails() {
        return accountDetails;
    }

    public void setAccountDetails(String accountDetails) {
        this.accountDetails = accountDetails;
    }

    public String getRemoveAccount() {
        return removeAccount;
    }

    public void setRemoveAccount(String removeAccount) {
        this.removeAccount = removeAccount;
    }

    private String cardAdded = "Card Added";
    private String chargeSmall = "We may charge a small amount(1.01) to confirm your card details.This is immediately refunded";
    private String Done = "Done";
    private String addPromo = "Add Promo";
    private String setLocationOnMapLabel = "Set location on map";
    private String extras = "Extras";
    private String done = "Done";
    private String enterDriverNotes = "Enter Driver Notes";
    private String adults = "ADULTS";
    private String journeyCharges = "(Journey Charges)";
    private String extraCharges = "(Extras Charges)";
    private String maxLimitExceed = "Maximum Limit Exceeded!";
    private String cash = "Cash";
    private String creditCard = "Credit Card";
    private String cardInCar = "Card In Car";

    public String getCardInCar() {
        return cardInCar;
    }

    public void setCardInCar(String cardInCar) {
        this.cardInCar = cardInCar;
    }

    private String expire = "Expire";
    private String addCard = "Add Card";
    private String appName = "Demo design theme";
    private String website = "Website";
    private String headerAuthorize = "Verify Mobile Number";
    private String verifyEmail = "Verify Email Address";
    private String subHeadingAuthorize = "Enter the 4 digit verification code you recieved from us via sms to";
    private String mobileNum = "07700651151";
    private String btnAuthorizeP1 = "Resend Code after ";
    private String btnAuthorizeResendCode = "Resend Code";
    private String btnAuthorizeP2 = " seconds";
    private String textConfrm = "Confirm";
    private String bookingStatus = "Booking Status";
    private String receipt = "Receipt";

    public String getTapToRateYourDriver() {
        return tapToRateYourDriver;
    }

    public void setTapToRateYourDriver(String tapToRateYourDriver) {
        this.tapToRateYourDriver = tapToRateYourDriver;
    }

    private String tapToRateYourDriver = "Tap To Rate Your Driver";

    public String getYouRated() {
        return youRated;
    }

    public void setYouRated(String youRated) {
        this.youRated = youRated;
    }

    private String youRated = "You Rated";
    private String bookingRef = "Booking Ref";
    private String bookingStatusValue = "bookingStatusValue";
    private String gettingLocation = "Getting Location...";
    private String pleaseWait = "Please wait...";
    private String journey = "Journey";
    private String fares = "Fare:";
    private String vehicle = "Vehicle";
    private String vehicleName = "Vehicle Name";
    private String noPlate = "No Plate";
    private String bookingsHeader = "Bookings";
    private String tabOne = "Scheduled";
    private String tabTwo = "History";
    private String textNoBooking = "No Bookings Available";
    private String ref = "Ref";
    private String trackDriver = "Track Driver";
    private String cancelBooking = "Cancel Booking";
    private String rebookNow = "Rebook Now";
    private String showDetails = "Show Details";
    private String yes = "Yes";
    private String No = "No";
    private String invalidPromoCode = "Invalid Promo Code";
    private String noDataFound = "No Data Found";
    private String gettingInformation = "Getting Information";
    private String areYouSure = "Are you sure?";
    private String wannaDeleteHome = "You want to delete your home location";
    private String wannaDeleteWork = "You want to delete your work location";
    private String deletedSuccessFully = "Deleted Successfully";
    private String homeLocationDeleted = "Home location has been deleted";
    private String workLocationDeleted = "Work location has been deleted";
    private String savingBooking = "Saving Booking";
    private String gettingDirections = "Getting Directions";
    private String gettingFares = "Getting Fares";
    private String requestCardDetails = "Requesting Card Details";
    private String updatingChanges = "Updating Changes";
    private String gettingBookingList = "Getting Booking List";
    private String areYouSureLogout = "Are you sure you want to logout?";
    private String areYouSureExit = "Are you sure you want to exit?";
    private String ok = "OK";
    private String loginFailed = "Login Failed";
    private String loginSucess = "Login Sucess";
    private String invalidAccountDetails = "Invalid Account Details";
    private String AccountAdded = "Account Added Successfully";
    private String CardAddedSucess = "Card Added Sucessfully";
    private String AccountVerified = "Account Verified";
    private String unableCancelBooking = "Unable to cancel booking";
    private String invalidPassword = " Invalid Password!";
    private String unableToConnect = " Unable to connect to service, Please check your internet connection";
    private String allFieldsRequired = " All fields are required";
    private String accDetailsNotFound = " Account details not found";
    private String enterLostItem = "Please enter lost item name.";
    private String detailsSent = "Details Sent Sucessfully";
    private String pleaseEnterCardDetails = "Please enter complete card details";
    private String unableToRetrieveData = "Unable to retrieve data from server";
    private String somethingWentWrongTry = "Something went wrong,  Please try again later";
    private String problemsgettingData = " Problems in getting data. Please check your internet connection";
    private String rotationOn = "Rotation ON";
    private String rotationoff = "Rotation OFF";
    private String noViaSelected = " No via point selected";
    private String locationNotSelected = " Location is not selected";
    private String noDropOffSelected = "Please select drop off location";
    private String selectPickUpPoints = " Please select pickup points";
    private String enterDetails = " Please enter order details first!";
    private String kindlySelectCorrect = "Kindly select the correct date and time";
    private String userCancelledPayment = "User Cancelled Payment";
    private String locationNeedsToEnable = "Location needs to enable to perform some action in this application";
    private String selectPickUpFirst = "Please set pickup address first.";
    private String pleaseSelectDropPoints = " Please select pickup and drop off points";
    private String enterReturnDate = " Please enter return date";
    private String invalidCardDetails = " Invalid Card Details";
    private String cardDeciled = "Card Declined!";
    private String notOperatingOutsideUk = "We aren't operating outside of UK";
    private String driverNotAvailable = "Driver contact number is not available";
    private String smsFailed = " SMS faild, please try again.";
    private String smsFailedTryAgain = " SMS faild, please try again later!";
    private String emailExists = "Email already exists";
    private String haventPickedImage = "You haven't picked Image";
    private String somethingWentWrong = "Something went wrong";
    private String invalidEmailPass = " Invalid Email/Password";
    private String pleaseEnterEmail = "Please Enter Email.";
    private String invalidNumber = "Invalid mobile number";
    private String invalidEmail = " Invalid Email.";
    private String pleaseEnterNumber = "Please Enter Number.";
    private String pleaseResendVC = "Please Resend Verification Code";
    private String codeSentSuccess = "Code sent successfully";
    private String inAppCalling = "In app calling is not active for some time";
    private String reciptSent = "Receipt sent successfully on your registered email.";
    private String reEnterCode = " Re-enter Code";
    private String weSentCode = " We have sent you a verification code on your number";
    private String accDetailsNoFound = "Account details not found";
    private String cardDetailsNotFound = "Card details not found";
    private String cardDeclined = " Card Declined!";
    private String cardAddedSucess = "Card Added Successfully!";
    private String accountVerified = "Account Verified";
    private String pleaseEnterChangePass = "Please Enter Change Password";
    private String pleaseEnterCurrentPass = "Please Enter Your Current Password.";
    private String pleaseEnterNewPass = "Please Enter Your New Password.";
    private String pleaseCnfrmNewPass = "Please Confirm Your New Password.";
    private String passNotMatched = "Password not matched!";
    private String headerMobile = "Enter Number";
    private String subHeadingMobile = "Enter your country and mobile number and we will send you a verification code via sms";
    private String emailHintEt = "Enter Email";
    private String nextBtnText = "Confirm";
    private String enterCodeHere = "Enter Code Here";
    private String Apply = "Apply";
    private String pleaseEnterCodeFirst = " Please enter code first!";
    private String tooManyInvalidAttemts = "Too Many Invalid Attempts!";
    private String attempt3InvalidCode = " You attempt 3 invalid code request so you cannot avail any promotion in this booking";
    private String verifyingCode = "Verifying Code";
    private String limitExceed = "Promotion Journey Limit Exceeded";
    private String forgetHeaderText = "Forgot Password?";
    private String forgetSubHeading = "Enter the email address which you have already registered with us and click 'SEND' to receive your password ";
    private String nextTextBtn = "NEXT";
    private String goodEvening = "Good Evening";
    private String letsGet = "Lets Get Started";
    private String home = "Home";
    private String addWork = "Add Work";
    private String Work = "Work";
    private String swipeUp = "Swipe up for more vehicles";
    private String swipeDown = "Swipe down to collapse";
    private String approx = "(Approx)";
    private String shopping = "Shopping";
    private String confirmBooking = "Schedule Booking";
    private String bookLater = "Book Later";
    private String asap = "Asap";
    private String schedule = "Schedule a Ride";
    private String confirm = "Confirm";
    private String mins = "mins";
    private String pickupTime = "Pickup Time";
    private String saveBooking = "Save Booking...";
    private String gettingFare = "Getting Fare...";
    private String weDontOperateInthisArea = "We aren't operating in this area";
    private String leftNavName = "Jeny Parker ";
    private String leftNavPhone = "+44 7700 651151";
    private String leftNavEmail = "+test@gmail.com";
    private String[] leftItemStrings = new String[]{"Your Trips", "Payment", "User Profile", "Invite Your Friend", "About", ""};
    private String signupHeaderText = "login";
    private String loginSubHeading = "Please login with your valid email and password";
    private String passwordHintEt = "Password";
    private String forgotPassText = "Forgot Password?";
    private String progressBarTextTv = "NEXT";
    private String name = "Name";
    private String mobile = "Mobile";
    private String email = "Email";
    private String changePassword = "Change Password";
    private String tapToChange = "Tap here to change password";
    private String promoTitle = "THIS IS PROMOTIONAL CODE TITLE ";
    private String promoCode = "CODE :";
    private String promoCodeValidTill = "This is promo code message valid till 23:00 24-2-2019 max discount is 100";
    private String journeysLeft = "Journeys Left :";
    private String startsFrom = "Starts From :";
    private String validTill = " Valid Till : ";
    private String maximumDiscount = "Maximum Discount :";
    private String minimumFaresToAvail = "Minimum fares to avail promotion :";
    private String noPromotionsAvailable = "No Promotions Available!";
    private String codeCopied = "Code copied!";
    private String feedback = "Feedback";
    private String writeYourFeedback = "Write your feedback here";

    public String getRateYourTrip() {
        return rateYourTrip;
    }

    public void setRateYourTrip(String rateYourTrip) {
        this.rateYourTrip = rateYourTrip;
    }

    private String rateYourTrip = "Rate your trip";
    private String Skip = "Skip";

    public String getVia() {
        return via;
    }

    public void setVia(String via) {
        this.via = via;
    }

    private String via = "Via";
    private String pleaseResetPass = "Please reset your password";
    private String enterCurrentPass = " Enter Current Password";
    private String enterNewPass = "Enter New Password";
    private String confirmNewPass = "Confirm New Password";
    private String next = "Next";
    private String pickUp = "Pick Up";
    private String dropOff = "Drop Off";
    private String addVia = "Add Via";
    private String whereTo = "Where To";
    private String addHome = "Add Home";
    private String work = "Work";
    private String registerAgreeText = "By registering you agree";
    private String nextTextTv = "Next";
    private String firstName = "First Name";
    private String lastName = "Last Name";
    private String firstNameHint = "Enter First Name";
    private String lastNameHint = "Enter Last Name";
    private String signupText = "Signup";
    private String nameTv = "Name";
    private String loginBtnText = "Login";
    private String registerBtnText = "Register";
    private String estTime = "Est Time";
    private String tapToCall = "Tap to call driver";
    private String required = "Required";
    private String weHaveSentYouEmail = "We have sent you a verification code on your email address";
    private String sentYouCode = "We have sent you a verification code on your number ";
    private String validatingCode = "Validating Code....";
    private String invalidCode = "Invalid Code";
    private String tryAgain = "Try Again";

    private String isThisCorrectNum = "is your mobile number correct?";
    private String isThisCorrectEmail = "is your email address correct?";
    private String reEnterMobile = "Re-enter Mobile No";
    private String reEnterEmail = "Re-enter email";
    private String EnterEmail = "Enter email";
    private String EnterNum = "Enter number";
    private String sendCode = "Send Code";
    private String Cancel = "Cancel";
    private String SIGNUP = "SIGNUP";
    private String pleaseCreateAcc = "Please create your account";
    private String requiredFirstName = "Required First Name";
    private String requiredLasrName = "Required Last Name";
    private String enterPass = "Required Password";
    private String enterEmailForCode = "Enter your email address and we will send you a verification code via email";
    private String enterEmailForCodeViaSms = "Enter your email address and we will send you a verification code via sms";
    private String sendingCode = "Sending verification code ....";
    private String registrationFailed = "Registration Failed";
    private String smsCodeOnNumber = "A sms verification code will be sent to your mobile number.";
    private String isCorrectNum = "is your mobile number correct?";
    private String smsCodeOnEmail = "A sms verification code will be sent to your email.";
    private String isCorrectEmail = "is your email correct?";
    private String verification = "Verification";
    private String Success = "Success";
    private String unableToGetDetails = "Unable to get details";
    private String userDetailsAreMissing = "User details are missing. Please enter user details";
    private String userDetails = "User Details";
    private String noActiveJourney = "No active journey was found";
    private String tracking = "Tracking";
    private String requirePermission = "Require Permission";
    private String enableRotationFor = "Enable screen rotation for perfect view";
    private String gotoSettings = "Goto Settings";
    private String saveFeedback = "Saving Feedback";
    private String reviewFromApp = "_Review From Customer App";
    private String plateNo = "Plate No:";
    private String history = "History";
    private String yourBooking = "Your booking";
    private String is = "Your is";
    private String areYouSureCancelBooking = "Are you sure you want to cancel booking?";
    private String confirming = "Confirming ...";
    private String Track_Drive = "Track Driver";
    private String Cancel_Booking = "Cancel Booking";
    private String Show_On_Map = "Track";
    private String Show_Details = "Show Details";
    private String Rebook_Now = "Rebook Now";
    private String ONBOARD = "ONBOARD";
    private String cancelling = "Cancelling";
    private String unableToCancelBooking = "Unable to cancel booking now, try again later.";
    private String gettinDetails = "Getting Details";
    private String messageSentSuccessfully = "Message Sent Successfully";


    private String flightDetails = "Flight Details";
    private String alreadyAtAirport = "Already at airport";
    private String selectFlightNo = "Select Flight No";
    private String gettingAirportDetails = "Getting Airport Details";


    public void setGettingAirportDetails(String gettingAirportDetails) {
        this.gettingAirportDetails = gettingAirportDetails;
    }

    public String getGettingAirportDetails() {
        return gettingAirportDetails;
    }

    public String getFlightDetails() {
        return flightDetails;
    }

    public void setFlightDetails(String flightDetails) {
        this.flightDetails = flightDetails;
    }

    public String getAlreadyAtAirport() {
        return alreadyAtAirport;
    }

    public void setAlreadyAtAirport(String alreadyAtAirport) {
        this.alreadyAtAirport = alreadyAtAirport;
    }

    public String getSelectFlightNo() {
        return selectFlightNo;
    }

    public void setSelectFlightNo(String selectFlightNo) {
        this.selectFlightNo = selectFlightNo;
    }

    public String getMessageSentSuccessfully() {
        return messageSentSuccessfully;
    }

    public void setMessageSentSuccessfully(String messageSentSuccessfully) {
        this.messageSentSuccessfully = messageSentSuccessfully;
    }

    public String getAreYouSureYouWantToCancelThisBooking() {
        return areYouSureYouWantToCancelThisBooking;
    }

    public void setAreYouSureYouWantToCancelThisBooking(String areYouSureYouWantToCancelThisBooking) {
        this.areYouSureYouWantToCancelThisBooking = areYouSureYouWantToCancelThisBooking;
    }

    private String areYouSureYouWantToCancelThisBooking = "Are you sure, you want to cancel this booking??";
    private String getCode = "Code : ";
    private String letsGetStarted = "lets get started";
    private String problemGettingData = "Problem in getting data. Please check internet connection or try again!";
    private String error = "Error";
    private String savingCardDetails = "Saving Card Details";
    private String removingCardDetails = "Removing Card Details";
    private String youWantToRemovePromoCodeFromThisJourney = "You want to remove promo code from this journey.";
    private String byRegisteringYouAgree = "By registering you agree ";

    public String getRemovingCardDetails() {
        return removingCardDetails;
    }

    public void setRemovingCardDetails(String removingCardDetails) {
        this.removingCardDetails = removingCardDetails;
    }

    // default Payment
    private String defaultPaymentSetupCard = "Setup Card";
    private String defaultPaymentUseCard = "Use Card";
    private String defaultPaymentTitleLabel = "Setup Default Payment";
    private String defaultPaymentHeadingLabel = "Please select payment method";
    private String defaultPaymentSubHeadingLabel = "Please select your suitable payment method for paying your rides.";

    // header
    private String yourTrips = "Your Trips";
    private String payment = "Payment";
    private String userProfile = "User Profile";
    private String inviteYourFriend = "Invite Your Friend";
    private String about = "About";
    private String promotions = "Promotions";
    private String choosePaymentMethodToAdd = "Choose a payment method to add";


    public void setChoosePaymentMethodToAdd(String choosePaymentMethodToAdd) {
        this.choosePaymentMethodToAdd = choosePaymentMethodToAdd;
    }

    public String getChoosePaymentMethodToAdd() {
        return choosePaymentMethodToAdd;
    }

    public String getYourTrips() {
        return yourTrips;
    }

    public void setYourTrips(String yourTrips) {
        this.yourTrips = yourTrips;
    }

    public String getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(String userProfile) {
        this.userProfile = userProfile;
    }

    public String getInviteYourFriend() {
        return inviteYourFriend;
    }

    public void setInviteYourFriend(String inviteYourFriend) {
        this.inviteYourFriend = inviteYourFriend;
    }

    public String getDefaultPaymentSetupCard() {
        return defaultPaymentSetupCard;
    }

    public void setDefaultPaymentSetupCard(String defaultPaymentSetupCard) {
        this.defaultPaymentSetupCard = defaultPaymentSetupCard;
    }

    public String getDefaultPaymentUseCard() {
        return defaultPaymentUseCard;
    }

    public void setDefaultPaymentUseCard(String defaultPaymentUseCard) {
        this.defaultPaymentUseCard = defaultPaymentUseCard;
    }

    public String getDefaultPaymentTitleLabel() {
        return defaultPaymentTitleLabel;
    }

    public void setDefaultPaymentTitleLabel(String defaultPaymentTitleLabel) {
        this.defaultPaymentTitleLabel = defaultPaymentTitleLabel;
    }

    public String getDefaultPaymentHeadingLabel() {
        return defaultPaymentHeadingLabel;
    }

    public void setDefaultPaymentHeadingLabel(String defaultPaymentHeadingLabel) {
        this.defaultPaymentHeadingLabel = defaultPaymentHeadingLabel;
    }

    public String getDefaultPaymentSubHeadingLabel() {
        return defaultPaymentSubHeadingLabel;
    }

    public void setDefaultPaymentSubHeadingLabel(String defaultPaymentSubHeadingLabel) {
        this.defaultPaymentSubHeadingLabel = defaultPaymentSubHeadingLabel;
    }

    public String getProblemGettingData() {
        return problemGettingData;
    }

    public void setProblemGettingData(String problemGettingData) {
        this.problemGettingData = problemGettingData;
    }

    public String getBtnAuthorizeResendCode() {
        return btnAuthorizeResendCode;
    }

    public void setBtnAuthorizeResendCode(String btnAuthorizeResendCode) {
        this.btnAuthorizeResendCode = btnAuthorizeResendCode;
    }

    public String getByRegisteringYouAgree() {
        return byRegisteringYouAgree;
    }

    public void setByRegisteringYouAgree(String byRegisteringYouAgree) {
        this.byRegisteringYouAgree = byRegisteringYouAgree;
    }

    public String getYouWantToRemovePromoCodeFromThisJourney() {
        return youWantToRemovePromoCodeFromThisJourney;
    }

    public void setYouWantToRemovePromoCodeFromThisJourney(String youWantToRemovePromoCodeFromThisJourney) {
        this.youWantToRemovePromoCodeFromThisJourney = youWantToRemovePromoCodeFromThisJourney;
    }

    public String getSavingCardDetails() {
        return savingCardDetails;
    }

    public void setSavingCardDetails(String savingCardDetails) {
        this.savingCardDetails = savingCardDetails;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getSwipeDown() {
        return swipeDown;
    }

    public void setSwipeDown(String swipeDown) {
        this.swipeDown = swipeDown;
    }

    public String getAddPromo() {
        return addPromo;
    }

    public void setAddPromo(String addPromo) {
        this.addPromo = addPromo;
    }

    public String getGetCode() {
        return getCode;
    }

    public void setGetCode(String getCode) {
        this.getCode = getCode;
    }

    public String getGettinDetails() {
        return gettinDetails;
    }

    public void setGettinDetails(String gettinDetails) {
        this.gettinDetails = gettinDetails;
    }

    public String getUnableToCancelBooking() {
        return unableToCancelBooking;
    }

    public void setUnableToCancelBooking(String unableToCancelBooking) {
        this.unableToCancelBooking = unableToCancelBooking;
    }

    public String getCancelling() {
        return cancelling;
    }

    public void setCancelling(String cancelling) {
        this.cancelling = cancelling;
    }

    public String getONBOARD() {
        return ONBOARD;
    }

    public void setONBOARD(String ONBOARD) {
        this.ONBOARD = ONBOARD;
    }

    public String getTrack_Drive() {
        return Track_Drive;
    }

    public void setTrack_Drive(String track_Drive) {
        Track_Drive = track_Drive;
    }

    public String getCancel_Booking() {
        return Cancel_Booking;
    }

    public void setCancel_Booking(String cancel_Booking) {
        Cancel_Booking = cancel_Booking;
    }

    public String getShow_On_Map() {
        return Show_On_Map;
    }

    public void setShow_On_Map(String show_On_Map) {
        Show_On_Map = show_On_Map;
    }

    public String getShow_Details() {
        return Show_Details;
    }

    public void setShow_Details(String show_Details) {
        Show_Details = show_Details;
    }

    public String getRebook_Now() {
        return Rebook_Now;
    }

    public void setRebook_Now(String rebook_Now) {
        Rebook_Now = rebook_Now;
    }

    public String getConfirming() {
        return confirming;
    }

    public void setConfirming(String confirming) {
        this.confirming = confirming;
    }

    public String getAreYouSureCancelBooking() {
        return areYouSureCancelBooking;
    }

    public void setAreYouSureCancelBooking(String areYouSureCancelBooking) {
        this.areYouSureCancelBooking = areYouSureCancelBooking;
    }

    public String getIs() {
        return is;
    }

    public void setIs(String is) {
        this.is = is;
    }

    public String getYourBooking() {
        return yourBooking;
    }

    public void setYourBooking(String yourBooking) {
        this.yourBooking = yourBooking;
    }

    public String getHistory() {
        return history;
    }

    public void setHistory(String history) {
        this.history = history;
    }

    public String getPlateNo() {
        return plateNo;
    }

    public void setPlateNo(String plateNo) {
        this.plateNo = plateNo;
    }

    public String getReviewFromApp() {
        return reviewFromApp;
    }

    public void setReviewFromApp(String reviewFromApp) {
        this.reviewFromApp = reviewFromApp;
    }

    public String getSaveFeedback() {
        return saveFeedback;
    }

    public void setSaveFeedback(String saveFeedback) {
        this.saveFeedback = saveFeedback;
    }

    public String getGotoSettings() {
        return gotoSettings;
    }

    public void setGotoSettings(String gotoSettings) {
        this.gotoSettings = gotoSettings;
    }

    public String getEnableRotationFor() {
        return enableRotationFor;
    }

    public void setEnableRotationFor(String enableRotationFor) {
        this.enableRotationFor = enableRotationFor;
    }

    public String getRequirePermission() {
        return requirePermission;
    }

    public void setRequirePermission(String requirePermission) {
        this.requirePermission = requirePermission;
    }

    public String getTracking() {
        return tracking;
    }

    public void setTracking(String tracking) {
        this.tracking = tracking;
    }

    public String getNoActiveJourney() {
        return noActiveJourney;
    }

    public void setNoActiveJourney(String noActiveJourney) {
        this.noActiveJourney = noActiveJourney;
    }

    public String getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(String userDetails) {
        this.userDetails = userDetails;
    }

    public String getUserDetailsAreMissing() {
        return userDetailsAreMissing;
    }

    public void setUserDetailsAreMissing(String userDetailsAreMissing) {
        this.userDetailsAreMissing = userDetailsAreMissing;
    }

    public String getUnableToGetDetails() {
        return unableToGetDetails;
    }

    public void setUnableToGetDetails(String unableToGetDetails) {
        this.unableToGetDetails = unableToGetDetails;
    }

    public String getSuccess() {
        return Success;
    }

    public void setSuccess(String success) {
        Success = success;
    }

    public String getVerification() {
        return verification;
    }

    public void setVerification(String verification) {
        this.verification = verification;
    }

    public String getIsCorrectNum() {
        return isCorrectNum;
    }

    public void setIsCorrectNum(String isCorrectNum) {
        this.isCorrectNum = isCorrectNum;
    }

    public String getSmsCodeOnEmail() {
        return smsCodeOnEmail;
    }

    public void setSmsCodeOnEmail(String smsCodeOnEmail) {
        this.smsCodeOnEmail = smsCodeOnEmail;
    }

    public String getIsCorrectEmail() {
        return isCorrectEmail;
    }

    public void setIsCorrectEmail(String isCorrectEmail) {
        this.isCorrectEmail = isCorrectEmail;
    }

    public String getSmsCodeOnNumber() {
        return smsCodeOnNumber;
    }

    public void setSmsCodeOnNumber(String smsCodeOnNumber) {
        this.smsCodeOnNumber = smsCodeOnNumber;
    }

    public String getRegistrationFailed() {
        return registrationFailed;
    }

    public void setRegistrationFailed(String registrationFailed) {
        this.registrationFailed = registrationFailed;
    }

    public String getSendingCode() {
        return sendingCode;
    }

    public void setSendingCode(String sendingCode) {
        this.sendingCode = sendingCode;
    }

    public String getEnterEmailForCodeViaSms() {
        return enterEmailForCodeViaSms;
    }

    public void setEnterEmailForCodeViaSms(String enterEmailForCodeViaSms) {
        this.enterEmailForCodeViaSms = enterEmailForCodeViaSms;
    }

    public String getEnterEmailForCode() {
        return enterEmailForCode;
    }

    public void setEnterEmailForCode(String enterEmailForCode) {
        this.enterEmailForCode = enterEmailForCode;
    }

    public String getEnterPass() {
        return enterPass;
    }

    public void setEnterPass(String enterPass) {
        this.enterPass = enterPass;
    }

    public String getRequiredFirstName() {
        return requiredFirstName;
    }

    public void setRequiredFirstName(String requiredFirstName) {
        this.requiredFirstName = requiredFirstName;
    }

    public String getRequiredLasrName() {
        return requiredLasrName;
    }

    public void setRequiredLasrName(String requiredLasrName) {
        this.requiredLasrName = requiredLasrName;
    }

    public String getPleaseCreateAcc() {
        return pleaseCreateAcc;
    }

    public void setPleaseCreateAcc(String pleaseCreateAcc) {
        this.pleaseCreateAcc = pleaseCreateAcc;
    }

    public String getSIGNUP() {
        return SIGNUP;
    }

    public void setSIGNUP(String SIGNUP) {
        this.SIGNUP = SIGNUP;
    }

    public String getCancel() {
        return Cancel;
    }

    public void setCancel(String cancel) {
        Cancel = cancel;
    }

    public String getSendCode() {
        return sendCode;
    }

    public void setSendCode(String sendCode) {
        this.sendCode = sendCode;
    }

    public String getEnterEmail() {
        return EnterEmail;
    }

    public void setEnterEmail(String enterEmail) {
        EnterEmail = enterEmail;
    }

    public String getEnterNum() {
        return EnterNum;
    }

    public void setEnterNum(String enterNum) {
        EnterNum = enterNum;
    }

    public String getReEnterMobile() {
        return reEnterMobile;
    }

    public void setReEnterMobile(String reEnterMobile) {
        this.reEnterMobile = reEnterMobile;
    }

    public String getReEnterEmail() {
        return reEnterEmail;
    }

    public void setReEnterEmail(String reEnterEmail) {
        this.reEnterEmail = reEnterEmail;
    }

    public String getIsThisCorrectNum() {
        return isThisCorrectNum;
    }

    public void setIsThisCorrectNum(String isThisCorrectNum) {
        this.isThisCorrectNum = isThisCorrectNum;
    }

    public String getIsThisCorrectEmail() {
        return isThisCorrectEmail;
    }

    public void setIsThisCorrectEmail(String isThisCorrectEmail) {
        this.isThisCorrectEmail = isThisCorrectEmail;
    }

    public String getTryAgain() {
        return tryAgain;
    }

    public void setTryAgain(String tryAgain) {
        this.tryAgain = tryAgain;
    }

    public String getInvalidCode() {
        return invalidCode;
    }

    public void setInvalidCode(String invalidCode) {
        this.invalidCode = invalidCode;
    }

    public String getValidatingCode() {
        return validatingCode;
    }

    public void setValidatingCode(String validatingCode) {
        this.validatingCode = validatingCode;
    }

    public String getVerifyEmail() {
        return verifyEmail;
    }

    public void setVerifyEmail(String verifyEmail) {
        this.verifyEmail = verifyEmail;
    }

    public String getWeHaveSentYouEmail() {
        return weHaveSentYouEmail;
    }

    public void setWeHaveSentYouEmail(String weHaveSentYouEmail) {
        this.weHaveSentYouEmail = weHaveSentYouEmail;
    }

    public String getSentYouCode() {
        return sentYouCode;
    }

    public void setSentYouCode(String sentYouCode) {
        this.sentYouCode = sentYouCode;
    }

    public String getRequired() {
        return required;
    }

    public void setRequired(String required) {
        this.required = required;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getContactAppName() {
        return contactAppName;
    }

    public void setContactAppName(String contactAppName) {
        this.contactAppName = contactAppName;
    }

    public String getEnterAccountId() {
        return enterAccountId;
    }

    public void setEnterAccountId(String enterAccountId) {
        this.enterAccountId = enterAccountId;
    }

    public String getEnterLoginId() {
        return enterLoginId;
    }

    public void setEnterLoginId(String enterLoginId) {
        this.enterLoginId = enterLoginId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddAccount() {
        return addAccount;
    }

    public void setAddAccount(String addAccount) {
        this.addAccount = addAccount;
    }

    public String getCardAdded() {
        return cardAdded;
    }

    public void setCardAdded(String cardAdded) {
        this.cardAdded = cardAdded;
    }

    public String getChargeSmall() {
        return chargeSmall;
    }

    public void setChargeSmall(String chargeSmall) {
        this.chargeSmall = chargeSmall;
    }

    public String getDone() {
        return Done;
    }

    public void setDone(String done) {
        Done = done;
    }

    public String getEnterDriverNotes() {
        return enterDriverNotes;
    }

    public void setEnterDriverNotes(String enterDriverNotes) {
        this.enterDriverNotes = enterDriverNotes;
    }

    public String getAdults() {
        return adults;
    }

    public void setAdults(String adults) {
        this.adults = adults;
    }

    public String getJourneyCharges() {
        return journeyCharges;
    }

    public void setJourneyCharges(String journeyCharges) {
        this.journeyCharges = journeyCharges;
    }

    public String getExtraCharges() {
        return extraCharges;
    }

    public void setExtraCharges(String extraCharges) {
        this.extraCharges = extraCharges;
    }

    public String getMaxLimitExceed() {
        return maxLimitExceed;
    }

    public void setMaxLimitExceed(String maxLimitExceed) {
        this.maxLimitExceed = maxLimitExceed;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public String getCash() {
        return cash;
    }

    public void setCash(String cash) {
        this.cash = cash;
    }

    public String getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(String creditCard) {
        this.creditCard = creditCard;
    }

    public String getExpire() {
        return expire;
    }

    public void setExpire(String expire) {
        this.expire = expire;
    }

    public String getAddCard() {
        return addCard;
    }

    public void setAddCard(String addCard) {
        this.addCard = addCard;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getHeaderAuthorize() {
        return headerAuthorize;
    }

    public void setHeaderAuthorize(String headerAuthorize) {
        this.headerAuthorize = headerAuthorize;
    }

    public String getSubHeadingAuthorize() {
        return subHeadingAuthorize;
    }

    public void setSubHeadingAuthorize(String subHeadingAuthorize) {
        this.subHeadingAuthorize = subHeadingAuthorize;
    }

    public String getMobileNum() {
        return mobileNum;
    }

    public void setMobileNum(String mobileNum) {
        this.mobileNum = mobileNum;
    }

    public String getBtnAuthorizeP1() {
        return btnAuthorizeP1;
    }

    public void setBtnAuthorizeP1(String btnAuthorizeP1) {
        this.btnAuthorizeP1 = btnAuthorizeP1;
    }

    public String getBtnAuthorizeP2() {
        return btnAuthorizeP2;
    }

    public void setBtnAuthorizeP2(String btnAuthorizeP2) {
        this.btnAuthorizeP2 = btnAuthorizeP2;
    }

    public String getTextConfrm() {
        return textConfrm;
    }

    public void setTextConfrm(String textConfrm) {
        this.textConfrm = textConfrm;
    }

    public String getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(String bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public String getReceipt() {
        return receipt;
    }

    public void setReceipt(String receipt) {
        this.receipt = receipt;
    }

    public String getBookingRef() {
        return bookingRef;
    }

    public void setBookingRef(String bookingRef) {
        this.bookingRef = bookingRef;
    }

    public String getBookingStatusValue() {
        return bookingStatusValue;
    }

    public void setBookingStatusValue(String bookingStatusValue) {
        this.bookingStatusValue = bookingStatusValue;
    }

    public String getGettingLocation() {
        return gettingLocation;
    }

    public void setGettingLocation(String gettingLocation) {
        this.gettingLocation = gettingLocation;
    }

    public String getPleaseWait() {
        return pleaseWait;
    }

    public void setPleaseWait(String pleaseWait) {
        this.pleaseWait = pleaseWait;
    }

    public String getJourney() {
        return journey;
    }

    public void setJourney(String journey) {
        this.journey = journey;
    }

    public String getFares() {
        return fares;
    }

    public void setFares(String fares) {
        this.fares = fares;
    }

    public String getVehicle() {
        return vehicle;
    }

    public void setVehicle(String vehicle) {
        this.vehicle = vehicle;
    }

    public String getVehicleName() {
        return vehicleName;
    }

    public void setVehicleName(String vehicleName) {
        this.vehicleName = vehicleName;
    }

    public String getNoPlate() {
        return noPlate;
    }

    public void setNoPlate(String noPlate) {
        this.noPlate = noPlate;
    }

    public String getBookingsHeader() {
        return bookingsHeader;
    }

    public void setBookingsHeader(String bookingsHeader) {
        this.bookingsHeader = bookingsHeader;
    }

    public String getTabOne() {
        return tabOne;
    }

    public void setTabOne(String tabOne) {
        this.tabOne = tabOne;
    }

    public String getTabTwo() {
        return tabTwo;
    }

    public void setTabTwo(String tabTwo) {
        this.tabTwo = tabTwo;
    }

    public String getTextNoBooking() {
        return textNoBooking;
    }

    public void setTextNoBooking(String textNoBooking) {
        this.textNoBooking = textNoBooking;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getTrackDriver() {
        return trackDriver;
    }

    public void setTrackDriver(String trackDriver) {
        this.trackDriver = trackDriver;
    }

    public String getCancelBooking() {
        return cancelBooking;
    }

    public void setCancelBooking(String cancelBooking) {
        this.cancelBooking = cancelBooking;
    }

    public String getRebookNow() {
        return rebookNow;
    }

    public void setRebookNow(String rebookNow) {
        this.rebookNow = rebookNow;
    }

    public String getShowDetails() {
        return showDetails;
    }

    public void setShowDetails(String showDetails) {
        this.showDetails = showDetails;
    }

    public String getYes() {
        return yes;
    }

    public void setYes(String yes) {
        this.yes = yes;
    }

    public String getNo() {
        return No;
    }

    public void setNo(String no) {
        No = no;
    }

    public String getInvalidPromoCode() {
        return invalidPromoCode;
    }

    public void setInvalidPromoCode(String invalidPromoCode) {
        this.invalidPromoCode = invalidPromoCode;
    }

    public String getNoDataFound() {
        return noDataFound;
    }

    public void setNoDataFound(String noDataFound) {
        this.noDataFound = noDataFound;
    }

    public String getGettingInformation() {
        return gettingInformation;
    }

    public void setGettingInformation(String gettingInformation) {
        this.gettingInformation = gettingInformation;
    }

    public String getAreYouSure() {
        return areYouSure;
    }

    public void setAreYouSure(String areYouSure) {
        this.areYouSure = areYouSure;
    }

    public String getWannaDeleteHome() {
        return wannaDeleteHome;
    }

    public void setWannaDeleteHome(String wannaDeleteHome) {
        this.wannaDeleteHome = wannaDeleteHome;
    }

    public String getWannaDeleteWork() {
        return wannaDeleteWork;
    }

    public void setWannaDeleteWork(String wannaDeleteWork) {
        this.wannaDeleteWork = wannaDeleteWork;
    }

    public String getDeletedSuccessFully() {
        return deletedSuccessFully;
    }

    public void setDeletedSuccessFully(String deletedSuccessFully) {
        this.deletedSuccessFully = deletedSuccessFully;
    }

    public String getHomeLocationDeleted() {
        return homeLocationDeleted;
    }

    public void setHomeLocationDeleted(String homeLocationDeleted) {
        this.homeLocationDeleted = homeLocationDeleted;
    }

    public String getWorkLocationDeleted() {
        return workLocationDeleted;
    }

    public void setWorkLocationDeleted(String workLocationDeleted) {
        this.workLocationDeleted = workLocationDeleted;
    }

    public String getSavingBooking() {
        return savingBooking;
    }

    public void setSavingBooking(String savingBooking) {
        this.savingBooking = savingBooking;
    }

    public String getGettingDirections() {
        return gettingDirections;
    }

    public void setGettingDirections(String gettingDirections) {
        this.gettingDirections = gettingDirections;
    }

    public String getGettingFares() {
        return gettingFares;
    }

    public void setGettingFares(String gettingFares) {
        this.gettingFares = gettingFares;
    }

    public String getRequestCardDetails() {
        return requestCardDetails;
    }

    public void setRequestCardDetails(String requestCardDetails) {
        this.requestCardDetails = requestCardDetails;
    }

    public String getUpdatingChanges() {
        return updatingChanges;
    }

    public void setUpdatingChanges(String updatingChanges) {
        this.updatingChanges = updatingChanges;
    }

    public String getGettingBookingList() {
        return gettingBookingList;
    }

    public void setGettingBookingList(String gettingBookingList) {
        this.gettingBookingList = gettingBookingList;
    }

    public String getAreYouSureLogout() {
        return areYouSureLogout;
    }

    public void setAreYouSureLogout(String areYouSureLogout) {
        this.areYouSureLogout = areYouSureLogout;
    }

    public String getAreYouSureExit() {
        return areYouSureExit;
    }

    public void setAreYouSureExit(String areYouSureExit) {
        this.areYouSureExit = areYouSureExit;
    }

    public String getOk() {
        return ok;
    }

    public void setOk(String ok) {
        this.ok = ok;
    }

    public String getLoginFailed() {
        return loginFailed;
    }

    public void setLoginFailed(String loginFailed) {
        this.loginFailed = loginFailed;
    }

    public String getLoginSucess() {
        return loginSucess;
    }

    public void setLoginSucess(String loginSucess) {
        this.loginSucess = loginSucess;
    }

    public String getInvalidAccountDetails() {
        return invalidAccountDetails;
    }

    public void setInvalidAccountDetails(String invalidAccountDetails) {
        this.invalidAccountDetails = invalidAccountDetails;
    }

    public String getAccountAdded() {
        return AccountAdded;
    }

    public void setAccountAdded(String accountAdded) {
        AccountAdded = accountAdded;
    }

    public String getCardAddedSucess() {
        return CardAddedSucess;
    }

    public void setCardAddedSucess(String cardAddedSucess) {
        CardAddedSucess = cardAddedSucess;
    }

    public String getAccountVerified() {
        return AccountVerified;
    }

    public void setAccountVerified(String accountVerified) {
        AccountVerified = accountVerified;
    }

    public String getPleaseEnterChangePass() {
        return pleaseEnterChangePass;
    }

    public void setPleaseEnterChangePass(String pleaseEnterChangePass) {
        this.pleaseEnterChangePass = pleaseEnterChangePass;
    }

    public String getPleaseEnterCurrentPass() {
        return pleaseEnterCurrentPass;
    }

    public void setPleaseEnterCurrentPass(String pleaseEnterCurrentPass) {
        this.pleaseEnterCurrentPass = pleaseEnterCurrentPass;
    }

    public String getPleaseEnterNewPass() {
        return pleaseEnterNewPass;
    }

    public void setPleaseEnterNewPass(String pleaseEnterNewPass) {
        this.pleaseEnterNewPass = pleaseEnterNewPass;
    }

    public String getPleaseCnfrmNewPass() {
        return pleaseCnfrmNewPass;
    }

    public void setPleaseCnfrmNewPass(String pleaseCnfrmNewPass) {
        this.pleaseCnfrmNewPass = pleaseCnfrmNewPass;
    }

    public String getPassNotMatched() {
        return passNotMatched;
    }

    public void setPassNotMatched(String passNotMatched) {
        this.passNotMatched = passNotMatched;
    }

    public String getHeaderMobile() {
        return headerMobile;
    }

    public void setHeaderMobile(String headerMobile) {
        this.headerMobile = headerMobile;
    }

    public String getSubHeadingMobile() {
        return subHeadingMobile;
    }

    public void setSubHeadingMobile(String subHeadingMobile) {
        this.subHeadingMobile = subHeadingMobile;
    }

    public String getEmailHintEt() {
        return emailHintEt;
    }

    public void setEmailHintEt(String emailHintEt) {
        this.emailHintEt = emailHintEt;
    }

    public String getNextBtnText() {
        return nextBtnText;
    }

    public void setNextBtnText(String nextBtnText) {
        this.nextBtnText = nextBtnText;
    }

    public String getEnterCodeHere() {
        return enterCodeHere;
    }

    public void setEnterCodeHere(String enterCodeHere) {
        this.enterCodeHere = enterCodeHere;
    }

    public String getApply() {
        return Apply;
    }

    public void setApply(String apply) {
        Apply = apply;
    }

    public String getPleaseEnterCodeFirst() {
        return pleaseEnterCodeFirst;
    }

    public void setPleaseEnterCodeFirst(String pleaseEnterCodeFirst) {
        this.pleaseEnterCodeFirst = pleaseEnterCodeFirst;
    }

    public String getTooManyInvalidAttemts() {
        return tooManyInvalidAttemts;
    }

    public void setTooManyInvalidAttemts(String tooManyInvalidAttemts) {
        this.tooManyInvalidAttemts = tooManyInvalidAttemts;
    }

    public String getAttempt3InvalidCode() {
        return attempt3InvalidCode;
    }

    public void setAttempt3InvalidCode(String attempt3InvalidCode) {
        this.attempt3InvalidCode = attempt3InvalidCode;
    }

    public String getVerifyingCode() {
        return verifyingCode;
    }

    public void setVerifyingCode(String verifyingCode) {
        this.verifyingCode = verifyingCode;
    }

    public String getLimitExceed() {
        return limitExceed;
    }

    public void setLimitExceed(String limitExceed) {
        this.limitExceed = limitExceed;
    }

    public String getForgetHeaderText() {
        return forgetHeaderText;
    }

    public void setForgetHeaderText(String forgetHeaderText) {
        this.forgetHeaderText = forgetHeaderText;
    }

    public String getForgetSubHeading() {
        return forgetSubHeading;
    }

    public void setForgetSubHeading(String forgetSubHeading) {
        this.forgetSubHeading = forgetSubHeading;
    }

    public String getNextTextBtn() {
        return nextTextBtn;
    }

    public void setNextTextBtn(String nextTextBtn) {
        this.nextTextBtn = nextTextBtn;
    }

    public String getGoodEvening() {
        return goodEvening;
    }

    public void setGoodEvening(String goodEvening) {
        this.goodEvening = goodEvening;
    }

    public String getLetsGet() {
        return letsGet;
    }

    public void setLetsGet(String letsGet) {
        this.letsGet = letsGet;
    }

    public String getHome() {
        return home;
    }

    public void setHome(String home) {
        this.home = home;
    }

    public String getAddWork() {
        return addWork;
    }

    public void setAddWork(String addWork) {
        this.addWork = addWork;
    }

    public String getWork() {
        return Work;
    }

    public void setWork(String work) {
        Work = work;
    }

    public String getRegisterAgreeText() {
        return registerAgreeText;
    }

    public void setRegisterAgreeText(String registerAgreeText) {
        this.registerAgreeText = registerAgreeText;
    }

    public String getNextTextTv() {
        return nextTextTv;
    }

    public void setNextTextTv(String nextTextTv) {
        this.nextTextTv = nextTextTv;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstNameHint() {
        return firstNameHint;
    }

    public void setFirstNameHint(String firstNameHint) {
        this.firstNameHint = firstNameHint;
    }

    public String getLastNameHint() {
        return lastNameHint;
    }

    public void setLastNameHint(String lastNameHint) {
        this.lastNameHint = lastNameHint;
    }

    public String getSignupText() {
        return signupText;
    }

    public void setSignupText(String signupText) {
        this.signupText = signupText;
    }

    public String getNameTv() {
        return nameTv;
    }

    public void setNameTv(String nameTv) {
        this.nameTv = nameTv;
    }

    public String getLoginBtnText() {
        return loginBtnText;
    }

    public void setLoginBtnText(String loginBtnText) {
        this.loginBtnText = loginBtnText;
    }

    public String getRegisterBtnText() {
        return registerBtnText;
    }

    public void setRegisterBtnText(String registerBtnText) {
        this.registerBtnText = registerBtnText;
    }

    public String getEstTime() {
        return estTime;
    }

    public void setEstTime(String estTime) {
        this.estTime = estTime;
    }

    public String getTapToCall() {
        return tapToCall;
    }

    public void setTapToCall(String tapToCall) {
        this.tapToCall = tapToCall;
    }

    public String getSwipeUp() {
        return swipeUp;
    }

    public void setSwipeUp(String swipeUp) {
        this.swipeUp = swipeUp;
    }

    public String getApprox() {
        return approx;
    }

    public void setApprox(String approx) {
        this.approx = approx;
    }

    public String getShopping() {
        return shopping;
    }

    public void setShopping(String shopping) {
        this.shopping = shopping;
    }

    public String getConfirmBooking() {
        return confirmBooking;
    }

    public void setConfirmBooking(String confirmBooking) {
        this.confirmBooking = confirmBooking;
    }

    public String getBookLater() {
        return bookLater;
    }

    public void setBookLater(String bookLater) {
        this.bookLater = bookLater;
    }

    public String getAsap() {
        return asap;
    }

    public void setAsap(String asap) {
        this.asap = asap;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public String getConfirm() {
        return confirm;
    }

    public void setConfirm(String confirm) {
        this.confirm = confirm;
    }

    public String getMins() {
        return mins;
    }

    public void setMins(String mins) {
        this.mins = mins;
    }

    public String getPickupTime() {
        return pickupTime;
    }

    public void setPickupTime(String pickupTime) {
        this.pickupTime = pickupTime;
    }

    public String getSaveBooking() {
        return saveBooking;
    }

    public void setSaveBooking(String saveBooking) {
        this.saveBooking = saveBooking;
    }

    public String getGettingFare() {
        return gettingFare;
    }

    public void setGettingFare(String gettingFare) {
        this.gettingFare = gettingFare;
    }

    public String getWeDontOperateInthisArea() {
        return weDontOperateInthisArea;
    }

    public void setWeDontOperateInthisArea(String weDontOperateInthisArea) {
        this.weDontOperateInthisArea = weDontOperateInthisArea;
    }

    public String getLeftNavName() {
        return leftNavName;
    }

    public void setLeftNavName(String leftNavName) {
        this.leftNavName = leftNavName;
    }

    public String getLeftNavPhone() {
        return leftNavPhone;
    }

    public void setLeftNavPhone(String leftNavPhone) {
        this.leftNavPhone = leftNavPhone;
    }

    public String getLeftNavEmail() {
        return leftNavEmail;
    }

    public void setLeftNavEmail(String leftNavEmail) {
        this.leftNavEmail = leftNavEmail;
    }

    public String[] getLeftItemStrings() {
        return leftItemStrings;
    }

    public void setLeftItemStrings(String[] leftItemStrings) {
        this.leftItemStrings = leftItemStrings;
    }

    public String getSignupHeaderText() {
        return signupHeaderText;
    }

    public void setSignupHeaderText(String signupHeaderText) {
        this.signupHeaderText = signupHeaderText;
    }

    public String getLoginSubHeading() {
        return loginSubHeading;
    }

    public void setLoginSubHeading(String loginSubHeading) {
        this.loginSubHeading = loginSubHeading;
    }

    public String getPasswordHintEt() {
        return passwordHintEt;
    }

    public void setPasswordHintEt(String passwordHintEt) {
        this.passwordHintEt = passwordHintEt;
    }

    public String getForgotPassText() {
        return forgotPassText;
    }

    public void setForgotPassText(String forgotPassText) {
        this.forgotPassText = forgotPassText;
    }

    public String getProgressBarTextTv() {
        return progressBarTextTv;
    }

    public void setProgressBarTextTv(String progressBarTextTv) {
        this.progressBarTextTv = progressBarTextTv;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getChangePassword() {
        return changePassword;
    }

    public void setChangePassword(String changePassword) {
        this.changePassword = changePassword;
    }

    public String getTapToChange() {
        return tapToChange;
    }

    public void setTapToChange(String tapToChange) {
        this.tapToChange = tapToChange;
    }

    public String getPromotions() {
        return promotions;
    }

    public void setPromotions(String promotions) {
        this.promotions = promotions;
    }

    public String getPromoTitle() {
        return promoTitle;
    }

    public void setPromoTitle(String promoTitle) {
        this.promoTitle = promoTitle;
    }

    public String getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(String promoCode) {
        this.promoCode = promoCode;
    }

    public String getPromoCodeValidTill() {
        return promoCodeValidTill;
    }

    public void setPromoCodeValidTill(String promoCodeValidTill) {
        this.promoCodeValidTill = promoCodeValidTill;
    }

    public String getJourneysLeft() {
        return journeysLeft;
    }

    public void setJourneysLeft(String journeysLeft) {
        this.journeysLeft = journeysLeft;
    }

    public String getStartsFrom() {
        return startsFrom;
    }

    public void setStartsFrom(String startsFrom) {
        this.startsFrom = startsFrom;
    }

    public String getValidTill() {
        return validTill;
    }

    public void setValidTill(String validTill) {
        this.validTill = validTill;
    }

    public String getMaximumDiscount() {
        return maximumDiscount;
    }

    public void setMaximumDiscount(String maximumDiscount) {
        this.maximumDiscount = maximumDiscount;
    }

    public String getMinimumFaresToAvail() {
        return minimumFaresToAvail;
    }

    public void setMinimumFaresToAvail(String minimumFaresToAvail) {
        this.minimumFaresToAvail = minimumFaresToAvail;
    }

    public String getNoPromotionsAvailable() {
        return noPromotionsAvailable;
    }

    public void setNoPromotionsAvailable(String noPromotionsAvailable) {
        this.noPromotionsAvailable = noPromotionsAvailable;
    }

    public String getCodeCopied() {
        return codeCopied;
    }

    public void setCodeCopied(String codeCopied) {
        this.codeCopied = codeCopied;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getWriteYourFeedback() {
        return writeYourFeedback;
    }

    public void setWriteYourFeedback(String writeYourFeedback) {
        this.writeYourFeedback = writeYourFeedback;
    }

    public String getSkip() {
        return Skip;
    }

    public void setSkip(String skip) {
        Skip = skip;
    }

    public String getPleaseResetPass() {
        return pleaseResetPass;
    }

    public void setPleaseResetPass(String pleaseResetPass) {
        this.pleaseResetPass = pleaseResetPass;
    }

    public String getEnterCurrentPass() {
        return enterCurrentPass;
    }

    public void setEnterCurrentPass(String enterCurrentPass) {
        this.enterCurrentPass = enterCurrentPass;
    }

    public String getEnterNewPass() {
        return enterNewPass;
    }

    public void setEnterNewPass(String enterNewPass) {
        this.enterNewPass = enterNewPass;
    }

    public String getConfirmNewPass() {
        return confirmNewPass;
    }

    public void setConfirmNewPass(String confirmNewPass) {
        this.confirmNewPass = confirmNewPass;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public String getPickUp() {
        return pickUp;
    }

    public void setPickUp(String pickUp) {
        this.pickUp = pickUp;
    }

    public String getDropOff() {
        return dropOff;
    }

    public void setDropOff(String dropOff) {
        this.dropOff = dropOff;
    }

    public String getAddVia() {
        return addVia;
    }

    public void setAddVia(String addVia) {
        this.addVia = addVia;
    }

    public String getWhereTo() {
        return whereTo;
    }

    public void setWhereTo(String whereTo) {
        this.whereTo = whereTo;
    }

    public String getAddHome() {
        return addHome;
    }

    public void setAddHome(String addHome) {
        this.addHome = addHome;
    }

    public String getUnableCancelBooking() {
        return unableCancelBooking;
    }

    public void setUnableCancelBooking(String unableCancelBooking) {
        this.unableCancelBooking = unableCancelBooking;
    }

    public String getInvalidPassword() {
        return invalidPassword;
    }

    public void setInvalidPassword(String invalidPassword) {
        this.invalidPassword = invalidPassword;
    }

    public String getUnableToConnect() {
        return unableToConnect;
    }

    public void setUnableToConnect(String unableToConnect) {
        this.unableToConnect = unableToConnect;
    }

    public String getAllFieldsRequired() {
        return allFieldsRequired;
    }

    public void setAllFieldsRequired(String allFieldsRequired) {
        this.allFieldsRequired = allFieldsRequired;
    }

    public String getAccDetailsNotFound() {
        return accDetailsNotFound;
    }

    public void setAccDetailsNotFound(String accDetailsNotFound) {
        this.accDetailsNotFound = accDetailsNotFound;
    }

    public String getEnterLostItem() {
        return enterLostItem;
    }

    public void setEnterLostItem(String enterLostItem) {
        this.enterLostItem = enterLostItem;
    }

    public String getDetailsSent() {
        return detailsSent;
    }

    public void setDetailsSent(String detailsSent) {
        this.detailsSent = detailsSent;
    }

    public String getPleaseEnterCardDetails() {
        return pleaseEnterCardDetails;
    }

    public void setPleaseEnterCardDetails(String pleaseEnterCardDetails) {
        this.pleaseEnterCardDetails = pleaseEnterCardDetails;
    }

    public String getUnableToRetrieveData() {
        return unableToRetrieveData;
    }

    public void setUnableToRetrieveData(String unableToRetrieveData) {
        this.unableToRetrieveData = unableToRetrieveData;
    }

    public String getSomethingWentWrongTry() {
        return somethingWentWrongTry;
    }

    public void setSomethingWentWrongTry(String somethingWentWrongTry) {
        this.somethingWentWrongTry = somethingWentWrongTry;
    }

    public String getProblemsgettingData() {
        return problemsgettingData;
    }

    public void setProblemsgettingData(String problemsgettingData) {
        this.problemsgettingData = problemsgettingData;
    }

    public String getRotationOn() {
        return rotationOn;
    }

    public void setRotationOn(String rotationOn) {
        this.rotationOn = rotationOn;
    }

    public String getRotationoff() {
        return rotationoff;
    }

    public void setRotationoff(String rotationoff) {
        this.rotationoff = rotationoff;
    }

    public String getNoViaSelected() {
        return noViaSelected;
    }

    public void setNoViaSelected(String noViaSelected) {
        this.noViaSelected = noViaSelected;
    }

    public String getLocationNotSelected() {
        return locationNotSelected;
    }

    public void setLocationNotSelected(String locationNotSelected) {
        this.locationNotSelected = locationNotSelected;
    }

    public String getNoDropOffSelected() {
        return noDropOffSelected;
    }

    public void setNoDropOffSelected(String noDropOffSelected) {
        this.noDropOffSelected = noDropOffSelected;
    }

    public String getSelectPickUpPoints() {
        return selectPickUpPoints;
    }

    public void setSelectPickUpPoints(String selectPickUpPoints) {
        this.selectPickUpPoints = selectPickUpPoints;
    }

    public String getEnterDetails() {
        return enterDetails;
    }

    public void setEnterDetails(String enterDetails) {
        this.enterDetails = enterDetails;
    }

    public String getKindlySelectCorrect() {
        return kindlySelectCorrect;
    }

    public void setKindlySelectCorrect(String kindlySelectCorrect) {
        this.kindlySelectCorrect = kindlySelectCorrect;
    }

    public String getUserCancelledPayment() {
        return userCancelledPayment;
    }

    public void setUserCancelledPayment(String userCancelledPayment) {
        this.userCancelledPayment = userCancelledPayment;
    }

    public String getLocationNeedsToEnable() {
        return locationNeedsToEnable;
    }

    public void setLocationNeedsToEnable(String locationNeedsToEnable) {
        this.locationNeedsToEnable = locationNeedsToEnable;
    }

    public String getSelectPickUpFirst() {
        return selectPickUpFirst;
    }

    public void setSelectPickUpFirst(String selectPickUpFirst) {
        this.selectPickUpFirst = selectPickUpFirst;
    }

    public String getPleaseSelectDropPoints() {
        return pleaseSelectDropPoints;
    }

    public void setPleaseSelectDropPoints(String pleaseSelectDropPoints) {
        this.pleaseSelectDropPoints = pleaseSelectDropPoints;
    }

    public String getEnterReturnDate() {
        return enterReturnDate;
    }

    public void setEnterReturnDate(String enterReturnDate) {
        this.enterReturnDate = enterReturnDate;
    }

    public String getInvalidCardDetails() {
        return invalidCardDetails;
    }

    public void setInvalidCardDetails(String invalidCardDetails) {
        this.invalidCardDetails = invalidCardDetails;
    }

    public String getCardDeciled() {
        return cardDeciled;
    }

    public void setCardDeciled(String cardDeciled) {
        this.cardDeciled = cardDeciled;
    }

    public String getNotOperatingOutsideUk() {
        return notOperatingOutsideUk;
    }

    public void setNotOperatingOutsideUk(String notOperatingOutsideUk) {
        this.notOperatingOutsideUk = notOperatingOutsideUk;
    }

    public String getDriverNotAvailable() {
        return driverNotAvailable;
    }

    public void setDriverNotAvailable(String driverNotAvailable) {
        this.driverNotAvailable = driverNotAvailable;
    }

    public String getSmsFailed() {
        return smsFailed;
    }

    public void setSmsFailed(String smsFailed) {
        this.smsFailed = smsFailed;
    }

    public String getSmsFailedTryAgain() {
        return smsFailedTryAgain;
    }

    public void setSmsFailedTryAgain(String smsFailedTryAgain) {
        this.smsFailedTryAgain = smsFailedTryAgain;
    }

    public String getEmailExists() {
        return emailExists;
    }

    public void setEmailExists(String emailExists) {
        this.emailExists = emailExists;
    }

    public String getHaventPickedImage() {
        return haventPickedImage;
    }

    public void setHaventPickedImage(String haventPickedImage) {
        this.haventPickedImage = haventPickedImage;
    }

    public String getSomethingWentWrong() {
        return somethingWentWrong;
    }

    public void setSomethingWentWrong(String somethingWentWrong) {
        this.somethingWentWrong = somethingWentWrong;
    }

    public String getInvalidEmailPass() {
        return invalidEmailPass;
    }

    public void setInvalidEmailPass(String invalidEmailPass) {
        this.invalidEmailPass = invalidEmailPass;
    }

    public String getPleaseEnterEmail() {
        return pleaseEnterEmail;
    }

    public void setPleaseEnterEmail(String pleaseEnterEmail) {
        this.pleaseEnterEmail = pleaseEnterEmail;
    }

    public String getSetLocationOnMapLabel() {
        return setLocationOnMapLabel;
    }

    public void setSetLocationOnMapLabel(String setLocationOnMapLabel) {
        this.setLocationOnMapLabel = setLocationOnMapLabel;
    }

    public String getInvalidNumber() {
        return invalidNumber;
    }

    public void setInvalidNumber(String invalidNumber) {
        this.invalidNumber = invalidNumber;
    }

    public String getInvalidEmail() {
        return invalidEmail;
    }

    public void setInvalidEmail(String invalidEmail) {
        this.invalidEmail = invalidEmail;
    }

    public String getPleaseEnterNumber() {
        return pleaseEnterNumber;
    }

    public void setPleaseEnterNumber(String pleaseEnterNumber) {
        this.pleaseEnterNumber = pleaseEnterNumber;
    }

    public String getPleaseResendVC() {
        return pleaseResendVC;
    }

    public void setPleaseResendVC(String pleaseResendVC) {
        this.pleaseResendVC = pleaseResendVC;
    }

    public String getCodeSentSuccess() {
        return codeSentSuccess;
    }

    public void setCodeSentSuccess(String codeSentSuccess) {
        this.codeSentSuccess = codeSentSuccess;
    }

    public String getInAppCalling() {
        return inAppCalling;
    }

    public void setInAppCalling(String inAppCalling) {
        this.inAppCalling = inAppCalling;
    }

    public String getReciptSent() {
        return reciptSent;
    }

    public void setReciptSent(String reciptSent) {
        this.reciptSent = reciptSent;
    }

    public String getReEnterCode() {
        return reEnterCode;
    }

    public void setReEnterCode(String reEnterCode) {
        this.reEnterCode = reEnterCode;
    }

    public String getWeSentCode() {
        return weSentCode;
    }

    public void setWeSentCode(String weSentCode) {
        this.weSentCode = weSentCode;
    }

    public String getAccDetailsNoFound() {
        return accDetailsNoFound;
    }

    public void setAccDetailsNoFound(String accDetailsNoFound) {
        this.accDetailsNoFound = accDetailsNoFound;
    }

    public String getCardDetailsNotFound() {
        return cardDetailsNotFound;
    }

    public void setCardDetailsNotFound(String cardDetailsNotFound) {
        this.cardDetailsNotFound = cardDetailsNotFound;
    }

    public String getCardDeclined() {
        return cardDeclined;
    }

    public void setCardDeclined(String cardDeclined) {
        this.cardDeclined = cardDeclined;
    }

    public String getExtras() {
        return extras;
    }

    public void setExtras(String extras) {
        this.extras = extras;
    }

    public String getLetsGetStarted() {
        return letsGetStarted;
    }

    public void setLetsGetStarted(String letsGetStarted) {
        this.letsGetStarted = letsGetStarted;
    }
}



package base.models;

public class Flight {
        private String ScheduleDateTime;
        private String Message;
        private String Date;
        private String ArrivalTerminal;
        private String ArrivingFrom;
        private String FlightNo;
        private String DefaultClientId;
        private String DateTime;
        private String Status;


        public String getMessage() {
            return Message;
        }

        public void setMessage(String message) {
            Message = message;
        }

        public String getScheduleDateTime() {
            return ScheduleDateTime;
        }

        public void setScheduleDateTime(String scheduleDateTime) {
            ScheduleDateTime = scheduleDateTime;
        }

        public String getDate() {
            return Date;
        }

        public void setDate(String date) {
            Date = date;
        }

        public String getArrivalTerminal() {
            return ArrivalTerminal;
        }

        public void setArrivalTerminal(String arrivalTerminal) {
            ArrivalTerminal = arrivalTerminal;
        }

        public String getArrivingFrom() {
            return ArrivingFrom;
        }

        public void setArrivingFrom(String arrivingFrom) {
            ArrivingFrom = arrivingFrom;
        }

        public String getFlightNo() {
            return FlightNo;
        }

        public void setFlightNo(String flightNo) {
            FlightNo = flightNo;
        }

        public String getDefaultClientId() {
            return DefaultClientId;
        }

        public void setDefaultClientId(String defaultClientId) {
            DefaultClientId = defaultClientId;
        }

        public String getDateTime() {
            return DateTime;
        }

        public void setDateTime(String dateTime) {
            DateTime = dateTime;
        }

        public String getStatus() {
            return Status;
        }

        public void setStatus(String status) {
            Status = status;
        }
    }
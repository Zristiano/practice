package yuanmengzeng.OA;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class ListingApp {
    static class SupplierAListing
    {
        private int id;
        private int ticketQuantity;
        private double ticketPrice;

        public SupplierAListing(int id, int ticketQuantity, double ticketPrice)
        {
            this.id = id;
            this.ticketQuantity = ticketQuantity;
            this.ticketPrice = ticketPrice;
        }

        public int getId() {
            return this.id;
        }

        public int getTicketQuantity() {
            return this.ticketQuantity;
        }

        public double getTicketPrice() {
            return this.ticketPrice;
        }
    }

    static class SupplierAApi
    {
        public int getEventId(String eventName)
        {
            return new Random().nextInt(1000);
        }

        public List<SupplierAListing> getAvailableListings(int eventId)
        {
            Random random = new Random();
            return Arrays.asList(
                    new SupplierAListing(random.nextInt(1000), random.nextInt(3) + 1, random.nextInt(100)),
                    new SupplierAListing(random.nextInt(1000), random.nextInt(4) + 1, random.nextInt(120))
            );
        }
    }

    static class SupplierBListing
    {
        private int listingId;
        private int availableTickets;
        private double totalPrice;

        public SupplierBListing(int listingId, int availableTickets, double totalPrice)
        {
            this.listingId = listingId;
            this.availableTickets = availableTickets;
            this.totalPrice = totalPrice;
        }

        public int getListingId() {
            return this.listingId;
        }

        public int getAvailableTickets() {
            return this.availableTickets;
        }

        public double getTotalPrice() {
            return this.totalPrice;
        }
    }

    static class SupplierBApi
    {
        public List<SupplierBListing> getListings(String eventName)
        {
            Random random = new Random();
            return Arrays.asList(
                    new SupplierBListing(random.nextInt(1000), random.nextInt(3) + 1, random.nextInt(100)),
                    new SupplierBListing(random.nextInt(1000), random.nextInt(2) + 1, random.nextInt(120)),
                    new SupplierBListing(random.nextInt(1000), random.nextInt(4) + 1, random.nextInt(80))
            );
        }
    }

    static class Event
    {
        private String name;

        public Event(String name)
        {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }

    static class Listing
    {
        private Event event;
        private int quantity;
        private double ticketPrice;
        private int supplierListingId;
        private String supplierName;

        public Listing(Event event, int quantity, double ticketPrice, int supplierListingId, String supplierName)
        {
            this.event = event;
            this.quantity = quantity;
            this.ticketPrice = ticketPrice;
            this.supplierListingId = supplierListingId;
            this.supplierName = supplierName;
        }

        public Event getEvent()
        {
            return this.event;
        }

        public int getQuantity()
        {
            return this.quantity;
        }

        public double getTicketPrice()
        {
            return this.ticketPrice;
        }

        public int getSupplierListingId()
        {
            return this.supplierListingId;
        }

        public String getSupplierName()
        {
            return this.supplierName;
        }
    }

    static class ViagogoApi
    {
        public List<Event> getEvents()
        {
            return Arrays.asList(
                    new Event("Beyonce at Madison Square Garden"),
                    new Event("Superbowl"),
                    new Event("Coachella")
            );
        }

        public void CreateListings(List<Listing> listings)
        {
            for (Listing l : listings)
            {
                System.out.format("created listing of %d ticket(s) for %s from %s%n", l.getQuantity(), l.getEvent().getName(), l.getSupplierName());
            }
        }
    }

    interface ListingGetter{
        int getId();
        int getTicketQuantity();
        double getTicketPrice();
        String getSupplierName();
        Event getEvent();
    }

    static class ListingAGetter implements ListingGetter{

        private Event event ;
        private SupplierAListing listing;

        ListingAGetter(Event event, SupplierAListing listingA){
            this.event = event;
            listing = listingA;
        }

        @Override
        public int getId() {
            return listing.getId();
        }

        @Override
        public int getTicketQuantity() {
            return listing.getTicketQuantity();
        }

        @Override
        public double getTicketPrice() {
            return listing.getTicketPrice();
        }

        @Override
        public String getSupplierName() {
            return "Supplier A";
        }

        @Override
        public Event getEvent() {
            return event;
        }
    }

    static class SupplierAListingProducer{
        SupplierAApi supplierA;

        SupplierAListingProducer(SupplierAApi supplierA){
            this.supplierA = supplierA;
        }

        List<ListingGetter> produceListingGetter(Event event){
            List<SupplierAListing> aListings = supplierA.getAvailableListings(supplierA.getEventId(event.getName()));
            List<ListingGetter> listingGetters = new ArrayList<>();
            for (SupplierAListing listing: aListings){
                listingGetters.add(new ListingAGetter(event,listing));
            }
            return listingGetters;
        }
    }

    static class ListingBGetter implements ListingGetter{

        private Event event ;
        private SupplierBListing listing;

        ListingBGetter(Event event, SupplierBListing listingb){
            this.event = event;
            listing = listingb;
        }

        @Override
        public int getId() {
            return listing.getListingId();
        }

        @Override
        public int getTicketQuantity() {
            return listing.getAvailableTickets();
        }

        @Override
        public double getTicketPrice() {
            return listing.getTotalPrice()/listing.getAvailableTickets();
        }

        @Override
        public String getSupplierName() {
            return "Supplier B";
        }

        @Override
        public Event getEvent() {
            return event;
        }
    }

    static class SupplierBListingProducer{
        SupplierBApi supplierB;

        SupplierBListingProducer(SupplierBApi supplierB){
            this.supplierB = supplierB;
        }

        List<ListingGetter> produceListingGetter(Event event){
            List<SupplierBListing> aListings = supplierB.getListings(event.getName());
            List<ListingGetter> listingGetters = new ArrayList<>();
            for (SupplierBListing listing: aListings){
                listingGetters.add(new ListingBGetter(event,listing));
            }
            return listingGetters;
        }
    }



    public static void main (String[] args) throws java.lang.Exception
        {
            ViagogoApi viagogo = new ViagogoApi();
            SupplierAListingProducer supplierAListingProducer = new SupplierAListingProducer(new SupplierAApi());
            SupplierBListingProducer supplierBListingProducer = new SupplierBListingProducer(new SupplierBApi());

            List<Event> events = viagogo.getEvents();
            List<Listing> listings = new ArrayList<Listing>();
            List<ListingGetter> listingGetters = new ArrayList<>();

            for (Event event:events) {
                listingGetters.addAll(supplierAListingProducer.produceListingGetter(event));
                listingGetters.addAll(supplierBListingProducer.produceListingGetter(event));
            }
            for (ListingGetter listingGetter : listingGetters){
                listings.add(new Listing(listingGetter.getEvent(), listingGetter.getTicketQuantity(), listingGetter.getTicketPrice(), listingGetter.getId(),listingGetter.getSupplierName()));
            }

            viagogo.CreateListings(listings);
        }

}

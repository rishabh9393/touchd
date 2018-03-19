package app.touched.com.touched.Models;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anshul on 2/23/2018.
 */
@IgnoreExtraProperties
public class User_Details implements Serializable {

    public static class Fb_Photos {
        private Fb_Photos_Details data;

        public Fb_Photos_Details getData() {
            return data;
        }

        public void setData(Fb_Photos_Details data) {
            this.data = data;
        }
    }

    public static class Fb_Photo_Images {
        private String height, source, width;

        public String getHeight() {
            return height;
        }

        public void setHeight(String height) {
            this.height = height;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getWidth() {
            return width;
        }

        public void setWidth(String width) {
            this.width = width;
        }
    }

    public static class Fb_Photos_Details {
        private String id, picture, updated_time;
        private ArrayList<Fb_Photo_Images> images;

        public ArrayList<Fb_Photo_Images> getImages() {
            return images;
        }

        public void setImages(ArrayList<Fb_Photo_Images> images) {
            this.images = images;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPicture() {
            return picture;
        }

        public void setPicture(String picture) {
            this.picture = picture;
        }

        public String getUpdated_time() {
            return updated_time;
        }

        public void setUpdated_time(String updated_time) {
            this.updated_time = updated_time;
        }
    }

    public static class School {
        private String id, name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class Education {
        private String id, type;
        private School school;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public School getSchool() {
            return school;
        }

        public void setSchool(School school) {
            this.school = school;
        }
    }

    public static class Location {
        private String id, name;
        private String Latitude;
        private String Longitude;

        public String getLatitude() {
            return Latitude;
        }

        public void setLatitude(String latitude) {
            Latitude = latitude;
        }

        public String getLongitude() {
            return Longitude;
        }

        public void setLongitude(String longitude) {
            Longitude = longitude;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class Picture {
        private Picture_Data data;

        public Picture_Data getData() {
            return data;
        }

        public void setData(Picture_Data data) {
            this.data = data;
        }
    }

    public static class Picture_Data {
        private String url, height, width;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getHeight() {
            return height;
        }

        public void setHeight(String height) {
            this.height = height;
        }

        public String getWidth() {
            return width;
        }

        public void setWidth(String width) {
            this.width = width;
        }
    }

    public static class Work {
        private String id, description, employer, location;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getEmployer() {
            return employer;
        }

        public void setEmployer(String employer) {
            this.employer = employer;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }
    }

    private String email;
    private String first_name;
    private String gender;
    private String last_name;
    private String about;
    private String address;
    private String birthday;
    private List<String> interested_in;
    private String id;
    private String age;
    private String ranking;
    private Integer points;
    private List<Education> education;
    private Location location;
    private Picture picture;
    private Work work;
    private String no_gifts;
    private String no_refunds;
    private String balance;
    private Fb_Photos photos;

    public Fb_Photos getPhotos() {
        return photos;
    }

    public void setPhotos(Fb_Photos photos) {
        this.photos = photos;
    }

    public String getNo_gifts() {
        return no_gifts;
    }

    public void setNo_gifts(String no_gifts) {
        this.no_gifts = no_gifts;
    }

    public String getNo_refunds() {
        return no_refunds;
    }

    public void setNo_refunds(String no_refunds) {
        this.no_refunds = no_refunds;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getAge() {
        return age;
    }

    public String getRanking() {
        return ranking;
    }

    public void setRanking(String ranking) {
        this.ranking = ranking;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }


    public Work getWork() {
        return work;
    }

    public void setWork(Work work) {
        this.work = work;
    }


    public void setAge(String dob) {

        this.age = dob;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Location getLocation() {
        return location;
    }

    public Picture getPicture() {
        return picture;
    }

    public void setPicture(Picture picture) {
        this.picture = picture;
    }


    public List<Education> getEducation() {
        return education;
    }

    public void setEducation(List<Education> education) {
        this.education = education;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getUser_id() {
        return id;
    }

    public void setUser_id(String user_id) {
        this.id = user_id;
    }

    public User_Details() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }


    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }


    public List<String> getInterested_in() {
        return interested_in;
    }

    public void setInterested_in(List<String> interested_in) {
        this.interested_in = interested_in;
    }

}

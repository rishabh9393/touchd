package app.touched.com.touched.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anshul on 2/23/2018.
 */
@IgnoreExtraProperties
public class User_Details implements Parcelable {

    public static class Fb_Photos implements Parcelable {

        private Fb_Photos_Details data;

        public Fb_Photos_Details getData() {
            return data;
        }

        public void setData(Fb_Photos_Details data) {
            this.data = data;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeParcelable(this.data, flags);
        }

        public Fb_Photos() {
        }

        protected Fb_Photos(Parcel in) {
            this.data = in.readParcelable(Fb_Photos_Details.class.getClassLoader());
        }

        public static final Creator<Fb_Photos> CREATOR = new Creator<Fb_Photos>() {
            @Override
            public Fb_Photos createFromParcel(Parcel source) {
                return new Fb_Photos(source);
            }

            @Override
            public Fb_Photos[] newArray(int size) {
                return new Fb_Photos[size];
            }
        };
    }

    public static class Fb_Photo_Images implements Parcelable {

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

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.height);
            dest.writeString(this.source);
            dest.writeString(this.width);
        }

        public Fb_Photo_Images() {
        }

        protected Fb_Photo_Images(Parcel in) {
            this.height = in.readString();
            this.source = in.readString();
            this.width = in.readString();
        }

        public static final Creator<Fb_Photo_Images> CREATOR = new Creator<Fb_Photo_Images>() {
            @Override
            public Fb_Photo_Images createFromParcel(Parcel source) {
                return new Fb_Photo_Images(source);
            }

            @Override
            public Fb_Photo_Images[] newArray(int size) {
                return new Fb_Photo_Images[size];
            }
        };
    }

    public static class Fb_Photos_Details implements Parcelable {

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

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.id);
            dest.writeString(this.picture);
            dest.writeString(this.updated_time);
            dest.writeTypedList(this.images);
        }

        public Fb_Photos_Details() {
        }

        protected Fb_Photos_Details(Parcel in) {
            this.id = in.readString();
            this.picture = in.readString();
            this.updated_time = in.readString();
            this.images = in.createTypedArrayList(Fb_Photo_Images.CREATOR);
        }

        public static final Creator<Fb_Photos_Details> CREATOR = new Creator<Fb_Photos_Details>() {
            @Override
            public Fb_Photos_Details createFromParcel(Parcel source) {
                return new Fb_Photos_Details(source);
            }

            @Override
            public Fb_Photos_Details[] newArray(int size) {
                return new Fb_Photos_Details[size];
            }
        };
    }

    public static class School implements Parcelable {

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

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.id);
            dest.writeString(this.name);
        }

        public School() {
        }

        protected School(Parcel in) {
            this.id = in.readString();
            this.name = in.readString();
        }

        public static final Creator<School> CREATOR = new Creator<School>() {
            @Override
            public School createFromParcel(Parcel source) {
                return new School(source);
            }

            @Override
            public School[] newArray(int size) {
                return new School[size];
            }
        };
    }

    public static class Education implements Parcelable {

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

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public boolean equals(Object obj) {
            boolean result = false;
            if (obj == null || obj.getClass() != getClass()) {
                result = false;
            } else if (obj instanceof Education) {
                Education employee = (Education) obj;
                if (this.school.getName().equals(employee.getSchool().getName())) {
                    result = true;
                }
            } else {
                return super.equals(obj);
            }
            return result;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.id);
            dest.writeString(this.type);
            dest.writeParcelable(this.school, flags);
        }

        public Education() {
        }

        protected Education(Parcel in) {
            this.id = in.readString();
            this.type = in.readString();
            this.school = in.readParcelable(School.class.getClassLoader());
        }

        public static final Creator<Education> CREATOR = new Creator<Education>() {
            @Override
            public Education createFromParcel(Parcel source) {
                return new Education(source);
            }

            @Override
            public Education[] newArray(int size) {
                return new Education[size];
            }
        };
    }

    public static class Location implements Parcelable {

        private String id, name;
        private String Latitude;
        private String Longitude;
        private float distance;

        public float getDistance() {
            return distance;
        }

        public void setDistance(float distance) {
            this.distance = distance;
        }

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

        public Location() {
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.id);
            dest.writeString(this.name);
            dest.writeString(this.Latitude);
            dest.writeString(this.Longitude);
            dest.writeFloat(this.distance);
        }

        protected Location(Parcel in) {
            this.id = in.readString();
            this.name = in.readString();
            this.Latitude = in.readString();
            this.Longitude = in.readString();
            this.distance = in.readFloat();
        }

        public static final Creator<Location> CREATOR = new Creator<Location>() {
            @Override
            public Location createFromParcel(Parcel source) {
                return new Location(source);
            }

            @Override
            public Location[] newArray(int size) {
                return new Location[size];
            }
        };
    }

    public static class Picture implements Parcelable {

        private Picture_Data data;

        public Picture_Data getData() {
            return data;
        }

        public void setData(Picture_Data data) {
            this.data = data;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeParcelable(this.data, flags);
        }

        public Picture() {
        }

        protected Picture(Parcel in) {
            this.data = in.readParcelable(Picture_Data.class.getClassLoader());
        }

        public static final Creator<Picture> CREATOR = new Creator<Picture>() {
            @Override
            public Picture createFromParcel(Parcel source) {
                return new Picture(source);
            }

            @Override
            public Picture[] newArray(int size) {
                return new Picture[size];
            }
        };
    }

    public static class Picture_Data implements Parcelable {

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

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.url);
            dest.writeString(this.height);
            dest.writeString(this.width);
        }

        public Picture_Data() {
        }

        protected Picture_Data(Parcel in) {
            this.url = in.readString();
            this.height = in.readString();
            this.width = in.readString();
        }

        public static final Creator<Picture_Data> CREATOR = new Creator<Picture_Data>() {
            @Override
            public Picture_Data createFromParcel(Parcel source) {
                return new Picture_Data(source);
            }

            @Override
            public Picture_Data[] newArray(int size) {
                return new Picture_Data[size];
            }
        };
    }

    public static class Work implements Parcelable {

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

        @Override
        public boolean equals(Object obj) {
            boolean result = false;
            if (obj == null || obj.getClass() != getClass()) {
                result = false;
            } else if (obj instanceof Work) {
                Work employee = (Work) obj;
                if (this.description.equals(employee.getDescription())) {
                    result = true;
                }
            } else {
                return super.equals(obj);
            }
            return result;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.id);
            dest.writeString(this.description);
            dest.writeString(this.employer);
            dest.writeString(this.location);
        }

        public Work() {
        }

        protected Work(Parcel in) {
            this.id = in.readString();
            this.description = in.readString();
            this.employer = in.readString();
            this.location = in.readString();
        }

        public static final Creator<Work> CREATOR = new Creator<Work>() {
            @Override
            public Work createFromParcel(Parcel source) {
                return new Work(source);
            }

            @Override
            public Work[] newArray(int size) {
                return new Work[size];
            }
        };
    }

    public static class Like implements Parcelable {
        private Picture picture;
        private String name;
        private String id;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Picture getPicture() {
            return picture;
        }

        public void setPicture(Picture picture) {
            this.picture = picture;
        }

        public Like() {
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeParcelable(this.picture, flags);
            dest.writeString(this.name);
            dest.writeString(this.id);
        }

        protected Like(Parcel in) {
            this.picture = in.readParcelable(Picture.class.getClassLoader());
            this.name = in.readString();
            this.id = in.readString();
        }

        public static final Creator<Like> CREATOR = new Creator<Like>() {
            @Override
            public Like createFromParcel(Parcel source) {
                return new Like(source);
            }

            @Override
            public Like[] newArray(int size) {
                return new Like[size];
            }
        };
    }

    public static class LikeList implements Parcelable {

        ArrayList<Like> data;

        public ArrayList<Like> getData() {
            return data;
        }

        public void setData(ArrayList<Like> data) {
            this.data = data;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeTypedList(this.data);
        }

        public LikeList() {
        }

        protected LikeList(Parcel in) {
            this.data = in.createTypedArrayList(Like.CREATOR);
        }

        public static final Creator<LikeList> CREATOR = new Creator<LikeList>() {
            @Override
            public LikeList createFromParcel(Parcel source) {
                return new LikeList(source);
            }

            @Override
            public LikeList[] newArray(int size) {
                return new LikeList[size];
            }
        };
    }

    public List<Work> getWork() {
        return work;
    }

    public void setWork(List<Work> work) {
        this.work = work;
    }

    private String is_login;
    private String email;
    private String first_name;
    private String gender;
    private String last_name;
    private String about;
    private String address;
    private String birthday;
    private List<String> interested_in;
    private String id;
    private int age;
    private String ranking;
    private Integer points;
    private List<Education> education;
    private Location location;
    private Picture picture;
    private List<Work> work;
    private String no_gifts;
    private String no_refunds;
    private String balance;
    private Fb_Photos photos;
    private String msg_count;
    private String key;
    private LikeList likes;

    public LikeList getLikes() {
        return likes;
    }

    public void setLikes(LikeList likes) {
        this.likes = likes;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getMsg_count() {
        return msg_count;
    }

    public void setMsg_count(String msg_count) {
        this.msg_count = msg_count;
    }

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

    public int getAge() {
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


    public void setAge(int dob) {

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

    public String getIs_login() {
        return is_login;
    }

    public void setIs_login(String is_login) {
        this.is_login = is_login;
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

    @Override
    public boolean equals(Object obj) {
        boolean result = false;
        if (obj == null || obj.getClass() != getClass()) {
            result = false;
        } else if (obj instanceof User_Details) {
            User_Details employee = (User_Details) obj;
            if (this.key.equals(employee.getKey())) {
                result = true;
            }
        } else {
            return super.equals(obj);
        }
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.is_login);
        dest.writeString(this.email);
        dest.writeString(this.first_name);
        dest.writeString(this.gender);
        dest.writeString(this.last_name);
        dest.writeString(this.about);
        dest.writeString(this.address);
        dest.writeString(this.birthday);
        dest.writeStringList(this.interested_in);
        dest.writeString(this.id);
        dest.writeInt(this.age);
        dest.writeString(this.ranking);
        dest.writeValue(this.points);
        dest.writeTypedList(this.education);
        dest.writeParcelable(this.location, flags);
        dest.writeParcelable(this.picture, flags);
        dest.writeTypedList(this.work);
        dest.writeString(this.no_gifts);
        dest.writeString(this.no_refunds);
        dest.writeString(this.balance);
        dest.writeParcelable(this.photos, flags);
        dest.writeString(this.msg_count);
        dest.writeString(this.key);
        dest.writeParcelable(this.likes, flags);
    }

    protected User_Details(Parcel in) {
        this.is_login = in.readString();
        this.email = in.readString();
        this.first_name = in.readString();
        this.gender = in.readString();
        this.last_name = in.readString();
        this.about = in.readString();
        this.address = in.readString();
        this.birthday = in.readString();
        this.interested_in = in.createStringArrayList();
        this.id = in.readString();
        this.age = in.readInt();
        this.ranking = in.readString();
        this.points = (Integer) in.readValue(Integer.class.getClassLoader());
        this.education = in.createTypedArrayList(Education.CREATOR);
        this.location = in.readParcelable(Location.class.getClassLoader());
        this.picture = in.readParcelable(Picture.class.getClassLoader());
        this.work = in.createTypedArrayList(Work.CREATOR);
        this.no_gifts = in.readString();
        this.no_refunds = in.readString();
        this.balance = in.readString();
        this.photos = in.readParcelable(Fb_Photos.class.getClassLoader());
        this.msg_count = in.readString();
        this.key = in.readString();
        this.likes = in.readParcelable(LikeList.class.getClassLoader());
    }

    public static final Creator<User_Details> CREATOR = new Creator<User_Details>() {
        @Override
        public User_Details createFromParcel(Parcel source) {
            return new User_Details(source);
        }

        @Override
        public User_Details[] newArray(int size) {
            return new User_Details[size];
        }
    };
}

package juju.android.jujucards;

/**
 * Created by tzimonjic on 5/3/17.
 */

public class User {
    private String name;
    private String age;
    private String gender;
    private String relationshipStatus;
    private Card cardPicked;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getRelationshipStatus() {
        return relationshipStatus;
    }

    public void setRelationshipStatus(String relationshipStatus) {
        this.relationshipStatus = relationshipStatus;
    }

    public Card getCardPicked() {
        return cardPicked;
    }

    public void setCardPicked(Card cardPicked) {
        this.cardPicked = cardPicked;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", cardPicked=" + cardPicked +
                '}';
    }
}

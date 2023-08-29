package poseidon.skills.citys;

import poseidon.skills.Chat.ChatAPI;
import poseidon.skills.Skills;

import java.util.HashMap;
import java.util.List;

public class nationOffer {
    public static HashMap<City, List<Nation>> offerdNation = new HashMap<>();
    private final City city;
    public nationOffer(City city){
        this.city = city;
    }
    public void addOffer(Nation nation){
        if(offerdNation.containsKey(this.city)){
            List<Nation> a = offerdNation.get(this.city);
            a.add(nation);
            offerdNation.replace(this.city, a);
        }
        else{
            List<Nation> b = List.of(nation);
            offerdNation.put(this.city, b);
        }
        ChatAPI.sendServerMessage(this.city, Skills.getInstance().message("Messages.Nation.addCity.reciveOffer").replace("{nation}", nation.getNationName()));

    }
    public boolean hasOffer(){
        return offerdNation.containsKey(this.city);
    }
    public List<Nation> getOffers(){
        return offerdNation.get(this.city);
    }
    public void offersDone(){
        offerdNation.remove(this.city);
    }

}

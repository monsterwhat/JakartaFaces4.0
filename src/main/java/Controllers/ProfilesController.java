package Controllers;

import Models.Profiles;
import Services.Service;
import Utils.FakeUserGenerator;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;
/**
 *
 * @author Al
 */

@Named(value = "ProfilesController")
@ViewScoped
public class ProfilesController implements Serializable{
    @Inject private Service profileService;

    private List<Profiles> profiles;
    private Profiles selectedProfile;
    private Profiles newProfile;
    private String selectedOption = "view";
    
    public ProfilesController() {
    }
    
    @PostConstruct
    public void init(){
        newProfile = new Profiles();
        selectedProfile = new Profiles();
    }
    
    public List<Profiles> profilesList(){
        if(profiles == null){
            profiles = profileService.listAll();
        }
        return profiles;
    }
    
    public long profileCount(){
        return profileService.count();
    }
    
    public void selectView(){
        selectedOption = "view";
    }
    
    public void selectEdit(){
        selectedOption = "edit";
    }
    
    public void selectCreate(){
        selectedOption = "create";
        openNewProfile();
    }
    
    public void openNewProfile(){
        newProfile = new Profiles();
    }
    
    public void updateProfile(){
        profileService.update(selectedProfile);
        clearSelectedProfile();
    }
    
    public void createProfile(){
        profileService.create(newProfile);
        clearSelectedProfile();        
    }
    
    public void deleteProfile(){
        if(selectedProfile != null){
        profileService.delete(selectedProfile);
        clearSelectedProfile();
        }
    }
    
    public void clearSelectedProfile(){
        profiles = null;
        newProfile = null;
        selectedProfile = null;
        selectedOption = "view";
    }
    
    public void generateAndCreateRandomUsers() {
        FakeUserGenerator userGenerator = new FakeUserGenerator();
        for (int i = 0; i < 10; i++) {
            Profiles newUser = userGenerator.generateFakeUserProfile();
            profileService.create(newUser);
        }
        clearSelectedProfile();
    }
    
    public void generateAndCreateRandomAdmins() {
        FakeUserGenerator userGenerator = new FakeUserGenerator();
        for (int i = 0; i < 10; i++) {
            Profiles newUser = userGenerator.generateFakeAdminProfile();
            profileService.create(newUser);
        }
        clearSelectedProfile();
    }

    public List<Profiles> getProfiles() {
        return profiles;
    }

    public void setProfiles(List<Profiles> profiles) {
        this.profiles = profiles;
    }

    public Profiles getSelectedProfile() {
        return selectedProfile;
    }

    public void setSelectedProfile(Profiles selectedProfile) {
        this.selectedProfile = selectedProfile;
    }

    public Profiles getNewProfile() {
        return newProfile;
    }

    public void setNewProfile(Profiles newProfile) {
        this.newProfile = newProfile;
    }

    public String getSelectedOption() {
        return selectedOption;
    }

    public void setSelectedOption(String selectedOption) {
        this.selectedOption = selectedOption;
    }
    
    
}

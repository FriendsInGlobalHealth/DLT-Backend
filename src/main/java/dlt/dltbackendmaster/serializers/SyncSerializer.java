package dlt.dltbackendmaster.serializers;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import dlt.dltbackendmaster.domain.Beneficiary;
import dlt.dltbackendmaster.domain.BeneficiaryIntervention;
import dlt.dltbackendmaster.domain.Locality;
import dlt.dltbackendmaster.domain.Neighborhood;
import dlt.dltbackendmaster.domain.Partners;
import dlt.dltbackendmaster.domain.Profiles;
import dlt.dltbackendmaster.domain.Service;
import dlt.dltbackendmaster.domain.SubService;
import dlt.dltbackendmaster.domain.Us;
import dlt.dltbackendmaster.domain.Users;
import dlt.dltbackendmaster.domain.watermelondb.BeneficiaryInterventionSyncModel;
import dlt.dltbackendmaster.domain.watermelondb.BeneficiarySyncModel;
import dlt.dltbackendmaster.domain.watermelondb.BeneficiaryVulnerabilitySyncModel;
import dlt.dltbackendmaster.domain.watermelondb.SyncObject;
import dlt.dltbackendmaster.domain.watermelondb.UsersSyncModel;

public class SyncSerializer
{

    /*
     * public static String createUsersSyncObject(List<Users> created, List<Users> updated, List<Integer> deleted) throws JsonProcessingException {
     * ObjectMapper mapper = new ObjectMapper();
     * List<ObjectNode> cretedlist = new ArrayList<ObjectNode>();
     * for (Users user : created) {
     * cretedlist.add(user.toObjectNode());
     * }
     * ArrayNode arrayCreated= mapper.createArrayNode();
     * arrayCreated.addAll(cretedlist);
     * List<ObjectNode> updatedlist = new ArrayList<ObjectNode>();
     * for (Users user : updated) {
     * updatedlist.add(user.toObjectNode());
     * }
     * ArrayNode arrayUpdated = mapper.createArrayNode();
     * arrayUpdated.addAll(updatedlist);
     * ArrayNode arrayDeleted= mapper.valueToTree(deleted);
     * ObjectNode userNode = mapper.createObjectNode();
     * userNode.set("created", arrayCreated);
     * userNode.set("updated", arrayUpdated);
     * userNode.set("deleted", arrayDeleted);
     * ObjectNode changesNode = mapper.createObjectNode();
     * changesNode.set("users", userNode);
     * Timestamp timestamp = new Timestamp(System.currentTimeMillis());
     * ObjectNode rootNode = mapper.createObjectNode();
     * rootNode.set("changes", changesNode);
     * rootNode.put("timestamp",timestamp.getTime());
     * return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);
     * }
     */
    public static String createSyncObject(SyncObject<Users> users, 
                                          SyncObject<Locality> localities, 
                                          SyncObject<Profiles> profiles, 
                                          SyncObject<Partners> partners, 
                                          SyncObject<Us> us, 
                                          SyncObject<Beneficiary> beneficiaries, 
                                          SyncObject<BeneficiaryIntervention> beneficiariesInterventions, 
                                          SyncObject<Neighborhood> neighborhoods, 
                                          SyncObject<Service> services, 
                                          SyncObject<SubService> subServices, 
                                          String lastPulledAt)
                    throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode changesNode = mapper.createObjectNode();
        changesNode.set("users", createUserSyncObject(users, lastPulledAt));
        changesNode.set("localities", createLocalitySyncObject(localities));
        changesNode.set("partners", createPartnersSyncObject(partners));
        changesNode.set("profiles", createProfilesSyncObject(profiles));
        changesNode.set("us", createUsSyncObject(us));
        changesNode.set("beneficiaries", createBeneficiarySyncObject(beneficiaries));
        changesNode.set("beneficiaries_interventions",
                        createBeneficiaryInterventionSyncObject(beneficiariesInterventions));
        changesNode.set("neighborhoods", createNeighborhoodSyncObject(neighborhoods));
        changesNode.set("services", createServiceSyncObject(services));
        changesNode.set("sub_services", createSubServiceSyncObject(subServices));
        
        ObjectNode rootNode = mapper.createObjectNode();
        rootNode.set("changes", changesNode);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        rootNode.put("timestamp", timestamp.getTime());
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);
    }

    public static ObjectNode createUserSyncObject(SyncObject<Users> users, String lastPulledAt)
                    throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        // created users
        List<Users> createdUsers = mapper.convertValue(users.getCreated(), new TypeReference<List<Users>>() {});
        List<ObjectNode> createdlist = createdUsers.stream()
                                                   .map((Users element) -> element.toObjectNode(lastPulledAt))
                                                   .collect(Collectors.toList());
        ArrayNode arrayCreated = mapper.createArrayNode();
        arrayCreated.addAll(createdlist);
        // updated users
        List<Users> updatedUsers = mapper.convertValue(users.getUpdated(), new TypeReference<List<Users>>() {});
        List<ObjectNode> updatedlist = updatedUsers.stream()
                                                   .map((Users element) -> element.toObjectNode(lastPulledAt))
                                                   .collect(Collectors.toList());
        ArrayNode arrayUpdated = mapper.createArrayNode();
        arrayUpdated.addAll(updatedlist);
        // deleted users
        // List<Integer> deletedUsers = mapper.convertValue(users.getDeleted(), new TypeReference<List<Integer>>() {});
        ArrayNode arrayDeleted = mapper.createArrayNode();
        // arrayDeleted.addAll(deletedUsers.toArray());
        ObjectNode userNode = mapper.createObjectNode();
        userNode.set("created", arrayCreated);
        userNode.set("updated", arrayUpdated);
        userNode.set("deleted", arrayDeleted);
        return userNode;
    }

    public static ObjectNode createLocalitySyncObject(SyncObject<Locality> locality) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        // created
        List<Locality> createdObjs = mapper.convertValue(locality.getCreated(), new TypeReference<List<Locality>>() {});
        List<ObjectNode> createdlist = createdObjs.stream()
                                                  .map((Locality element) -> element.toObjectNode())
                                                  .collect(Collectors.toList());
        ArrayNode arrayCreated = mapper.createArrayNode();
        arrayCreated.addAll(createdlist);
        // updated
        List<Locality> updatedObjs = mapper.convertValue(locality.getUpdated(), new TypeReference<List<Locality>>() {});
        List<ObjectNode> updatedlist = updatedObjs.stream()
                                                  .map((Locality element) -> element.toObjectNode())
                                                  .collect(Collectors.toList());
        ArrayNode arrayUpdated = mapper.createArrayNode();
        arrayUpdated.addAll(updatedlist);
        // deleted
        List<Integer> deletedObjs = mapper.convertValue(locality.getDeleted(), new TypeReference<List<Integer>>() {});
        ArrayNode arrayDeleted = mapper.valueToTree(deletedObjs);
        ObjectNode userNode = mapper.createObjectNode();
        userNode.set("created", arrayCreated);
        userNode.set("updated", arrayUpdated);
        userNode.set("deleted", arrayDeleted);
        return userNode;
    }

    public static ObjectNode createPartnersSyncObject(SyncObject<Partners> partners) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        // created
        List<Partners> createdObjs = mapper.convertValue(partners.getCreated(), new TypeReference<List<Partners>>() {});
        List<ObjectNode> createdlist = createdObjs.stream()
                                                  .map((Partners element) -> element.toObjectNode())
                                                  .collect(Collectors.toList());
        ArrayNode arrayCreated = mapper.createArrayNode();
        arrayCreated.addAll(createdlist);
        // updated
        List<Partners> updatedObjs = mapper.convertValue(partners.getUpdated(), new TypeReference<List<Partners>>() {});
        List<ObjectNode> updatedlist = updatedObjs.stream()
                                                  .map((Partners element) -> element.toObjectNode())
                                                  .collect(Collectors.toList());
        ArrayNode arrayUpdated = mapper.createArrayNode();
        arrayUpdated.addAll(updatedlist);
        // deleted
        List<Integer> deletedObjs = mapper.convertValue(partners.getDeleted(), new TypeReference<List<Integer>>() {});
        ArrayNode arrayDeleted = mapper.valueToTree(deletedObjs);
        ObjectNode userNode = mapper.createObjectNode();
        userNode.set("created", arrayCreated);
        userNode.set("updated", arrayUpdated);
        userNode.set("deleted", arrayDeleted);
        return userNode;
    }

    public static ObjectNode createProfilesSyncObject(SyncObject<Profiles> profiles) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        // created
        List<Profiles> createdObjs = mapper.convertValue(profiles.getCreated(), new TypeReference<List<Profiles>>() {});
        List<ObjectNode> createdlist = createdObjs.stream()
                                                  .map((Profiles element) -> element.toObjectNode())
                                                  .collect(Collectors.toList());
        ArrayNode arrayCreated = mapper.createArrayNode();
        arrayCreated.addAll(createdlist);
        // updated
        List<Profiles> updatedObjs = mapper.convertValue(profiles.getUpdated(), new TypeReference<List<Profiles>>() {});
        List<ObjectNode> updatedlist = updatedObjs.stream()
                                                  .map((Profiles element) -> element.toObjectNode())
                                                  .collect(Collectors.toList());
        ArrayNode arrayUpdated = mapper.createArrayNode();
        arrayUpdated.addAll(updatedlist);
        // deleted
        List<Integer> deletedObjs = mapper.convertValue(profiles.getDeleted(), new TypeReference<List<Integer>>() {});
        ArrayNode arrayDeleted = mapper.valueToTree(deletedObjs);
        ObjectNode userNode = mapper.createObjectNode();
        userNode.set("created", arrayCreated);
        userNode.set("updated", arrayUpdated);
        userNode.set("deleted", arrayDeleted);
        return userNode;
    }

    public static ObjectNode createUsSyncObject(SyncObject<Us> us) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        // created
        List<Us> createdObjs = mapper.convertValue(us.getCreated(), new TypeReference<List<Us>>() {});
        List<ObjectNode> createdlist = createdObjs.stream()
                                                  .map((Us element) -> element.toObjectNode())
                                                  .collect(Collectors.toList());
        ArrayNode arrayCreated = mapper.createArrayNode();
        arrayCreated.addAll(createdlist);
        // updated
        List<Us> updatedObjs = mapper.convertValue(us.getUpdated(), new TypeReference<List<Us>>() {});
        List<ObjectNode> updatedlist = updatedObjs.stream()
                                                  .map((Us element) -> element.toObjectNode())
                                                  .collect(Collectors.toList());
        ArrayNode arrayUpdated = mapper.createArrayNode();
        arrayUpdated.addAll(updatedlist);
        // deleted
        List<Integer> deletedObjs = mapper.convertValue(us.getDeleted(), new TypeReference<List<Integer>>() {});
        ArrayNode arrayDeleted = mapper.valueToTree(deletedObjs);
        ObjectNode userNode = mapper.createObjectNode();
        userNode.set("created", arrayCreated);
        userNode.set("updated", arrayUpdated);
        userNode.set("deleted", arrayDeleted);
        return userNode;
    }

    private static ObjectNode createBeneficiarySyncObject(SyncObject<Beneficiary> beneficiaries)
                    throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        List<Beneficiary> createdObjs = mapper.convertValue(beneficiaries.getCreated(),
                                                            new TypeReference<List<Beneficiary>>() {});
        List<ObjectNode> createdList = createdObjs.stream()
                                                  .map((Beneficiary element) -> element.toObjectNode())
                                                  .collect(Collectors.toList());
        ArrayNode arrayCreated = mapper.createArrayNode();
        arrayCreated.addAll(createdList);
        List<Beneficiary> updatedObjs = mapper.convertValue(beneficiaries.getUpdated(),
                                                            new TypeReference<List<Beneficiary>>() {});
        List<ObjectNode> updatedList = updatedObjs.stream()
                                                  .map((Beneficiary element) -> element.toObjectNode())
                                                  .collect(Collectors.toList());
        ArrayNode arrayUpdated = mapper.createArrayNode();
        arrayUpdated.addAll(updatedList);
        List<Integer> deletedObjs = mapper.convertValue(beneficiaries.getDeleted(),
                                                        new TypeReference<List<Integer>>() {});
        ArrayNode arrayDeleted = mapper.valueToTree(deletedObjs);
        ObjectNode beneficiaryNode = mapper.createObjectNode();
        beneficiaryNode.set("created", arrayCreated);
        beneficiaryNode.set("updated", arrayUpdated);
        beneficiaryNode.set("deleted", arrayDeleted);
        return beneficiaryNode;
    }

    private static ObjectNode createBeneficiaryInterventionSyncObject(SyncObject<BeneficiaryIntervention> beneficiariesInterventions)
                    throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        
        // FIXME: This method is setting beneficiary to null... do we really need to use it???
        //        List<BeneficiaryIntervention> createdObjs = mapper.convertValue(beneficiariesInterventions.getCreated(),
        //                                                                        new TypeReference<List<BeneficiaryIntervention>>() {});
        List<BeneficiaryIntervention> createdObjs = beneficiariesInterventions.getCreated();
        List<ObjectNode> createdList = createdObjs.stream()
                                                  .map((BeneficiaryIntervention element) -> element.toObjectNode())
                                                  .collect(Collectors.toList());
        ArrayNode arrayCreated = mapper.createArrayNode();
        arrayCreated.addAll(createdList);
        
        // FIXME: This method is setting beneficiary to null... do we really need to use it???
        //        List<BeneficiaryIntervention> updatedObjs = mapper.convertValue(beneficiariesInterventions.getUpdated(),
        //                                                                        new TypeReference<List<BeneficiaryIntervention>>() {});
        List<BeneficiaryIntervention> updatedObjs= beneficiariesInterventions.getUpdated();
        List<ObjectNode> updatedList = updatedObjs.stream()
                                                  .map((BeneficiaryIntervention element) -> element.toObjectNode())
                                                  .collect(Collectors.toList());
        ArrayNode arrayUpdated = mapper.createArrayNode();
        arrayUpdated.addAll(updatedList);
        List<Integer> deletedObjs = mapper.convertValue(beneficiariesInterventions.getDeleted(),
                                                        new TypeReference<List<Integer>>() {});
        ArrayNode arrayDeleted = mapper.valueToTree(deletedObjs);
        ObjectNode beneficiaryInterventionNode = mapper.createObjectNode();
        beneficiaryInterventionNode.set("created", arrayCreated);
        beneficiaryInterventionNode.set("updated", arrayUpdated);
        beneficiaryInterventionNode.set("deleted", arrayDeleted);
        return beneficiaryInterventionNode;
    }

    /*private static ObjectNode createBeneficiaryVulnerabilitySyncObject(SyncObject<BeneficiaryVulnerability> beneficiariesVulnerabilities)
                    throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        
        // FIXME: This method is setting beneficiary to null... do we really need to use it???
        //        List<BeneficiaryVulnerability> createdObjs = mapper.convertValue(beneficiariesVulnerabilities.getCreated(),
        //                                                                         new TypeReference<List<BeneficiaryVulnerability>>() {});
        List<BeneficiaryVulnerability> createdObjs = beneficiariesVulnerabilities.getCreated();
        List<ObjectNode> createdList = createdObjs.stream()
                                                  .map((BeneficiaryVulnerability element) -> element.toObjectNode())
                                                  .collect(Collectors.toList());
        ArrayNode arrayCreated = mapper.createArrayNode();
        arrayCreated.addAll(createdList);
        
        // FIXME: This method is setting beneficiary to null... do we really need to use it???
        //        List<BeneficiaryVulnerability> updatedObjs = mapper.convertValue(beneficiariesVulnerabilities.getUpdated(),
        //                                                                         new TypeReference<List<BeneficiaryVulnerability>>() {});
        List<BeneficiaryVulnerability> updatedObjs = beneficiariesVulnerabilities.getUpdated();
        List<ObjectNode> updatedList = updatedObjs.stream()
                                                  .map((BeneficiaryVulnerability element) -> element.toObjectNode())
                                                  .collect(Collectors.toList());
        ArrayNode arrayUpdated = mapper.createArrayNode();
        arrayUpdated.addAll(updatedList);
        List<Integer> deletedObjs = mapper.convertValue(beneficiariesVulnerabilities.getDeleted(),
                                                        new TypeReference<List<Integer>>() {});
        ArrayNode arrayDeleted = mapper.valueToTree(deletedObjs);
        ObjectNode beneficiaryVulnerabilityNode = mapper.createObjectNode();
        beneficiaryVulnerabilityNode.set("created", arrayCreated);
        beneficiaryVulnerabilityNode.set("updated", arrayUpdated);
        beneficiaryVulnerabilityNode.set("deleted", arrayDeleted);
        return beneficiaryVulnerabilityNode;
    }*/

    private static ObjectNode createNeighborhoodSyncObject(SyncObject<Neighborhood> neighborhood)
                    throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        List<Neighborhood> createdObjs = mapper.convertValue(neighborhood.getCreated(),
                                                             new TypeReference<List<Neighborhood>>() {});
        List<ObjectNode> createdList = createdObjs.stream()
                                                  .map((Neighborhood element) -> element.toObjectNode())
                                                  .collect(Collectors.toList());
        ArrayNode arrayCreated = mapper.createArrayNode();
        arrayCreated.addAll(createdList);
        List<Neighborhood> updatedObjs = mapper.convertValue(neighborhood.getUpdated(),
                                                             new TypeReference<List<Neighborhood>>() {});
        List<ObjectNode> updatedList = updatedObjs.stream()
                                                  .map((Neighborhood element) -> element.toObjectNode())
                                                  .collect(Collectors.toList());
        ArrayNode arrayUpdated = mapper.createArrayNode();
        arrayUpdated.addAll(updatedList);
        List<Integer> deletedObjs = mapper.convertValue(neighborhood.getDeleted(),
                                                        new TypeReference<List<Integer>>() {});
        ArrayNode arrayDeleted = mapper.valueToTree(deletedObjs);
        ObjectNode neighborhoodNode = mapper.createObjectNode();
        neighborhoodNode.set("created", arrayCreated);
        neighborhoodNode.set("updated", arrayUpdated);
        neighborhoodNode.set("deleted", arrayDeleted);
        return neighborhoodNode;
    }

    private static ObjectNode createServiceSyncObject(SyncObject<Service> service) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        List<Service> createdObjs = mapper.convertValue(service.getCreated(), new TypeReference<List<Service>>() {});
        List<ObjectNode> createdList = createdObjs.stream()
                                                  .map((Service element) -> element.toObjectNode())
                                                  .collect(Collectors.toList());
        ArrayNode arrayCreated = mapper.createArrayNode();
        arrayCreated.addAll(createdList);
        List<Service> updatedObjs = mapper.convertValue(service.getUpdated(), new TypeReference<List<Service>>() {});
        List<ObjectNode> updatedList = updatedObjs.stream()
                                                  .map((Service element) -> element.toObjectNode())
                                                  .collect(Collectors.toList());
        ArrayNode arrayUpdated = mapper.createArrayNode();
        arrayUpdated.addAll(updatedList);
        List<Integer> deletedObjs = mapper.convertValue(service.getDeleted(), new TypeReference<List<Integer>>() {});
        ArrayNode arrayDeleted = mapper.valueToTree(deletedObjs);
        ObjectNode serviceNode = mapper.createObjectNode();
        serviceNode.set("created", arrayCreated);
        serviceNode.set("updated", arrayUpdated);
        serviceNode.set("deleted", arrayDeleted);
        return serviceNode;
    }

    private static ObjectNode createSubServiceSyncObject(SyncObject<SubService> subService)
                    throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        List<SubService> createdObjs = mapper.convertValue(subService.getCreated(),
                                                           new TypeReference<List<SubService>>() {});
        List<ObjectNode> createdList = createdObjs.stream()
                                                  .map((SubService element) -> element.toObjectNode())
                                                  .collect(Collectors.toList());
        ArrayNode arrayCreated = mapper.createArrayNode();
        arrayCreated.addAll(createdList);
        List<SubService> updatedObjs = mapper.convertValue(subService.getUpdated(),
                                                           new TypeReference<List<SubService>>() {});
        List<ObjectNode> updatedList = updatedObjs.stream()
                                                  .map((SubService element) -> element.toObjectNode())
                                                  .collect(Collectors.toList());
        ArrayNode arrayUpdated = mapper.createArrayNode();
        arrayUpdated.addAll(updatedList);
        List<Integer> deletedObjs = mapper.convertValue(subService.getDeleted(), new TypeReference<List<Integer>>() {});
        ArrayNode arrayDeleted = mapper.valueToTree(deletedObjs);
        ObjectNode subServiceNode = mapper.createObjectNode();
        subServiceNode.set("created", arrayCreated);
        subServiceNode.set("updated", arrayUpdated);
        subServiceNode.set("deleted", arrayDeleted);
        return subServiceNode;
    }


    public static SyncObject readUsersSyncObject(String changes) throws JsonMappingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(changes);
        JsonNode changesNode = root.path("changes");

        if (!changesNode.isMissingNode()) {
            JsonNode usersNode = changesNode.path("users");
            @SuppressWarnings("unchecked")
            SyncObject<UsersSyncModel> users = mapper.treeToValue(usersNode,
                                                                  (Class<SyncObject<UsersSyncModel>>) (Object) SyncObject.class);
            return users;
        }
        return null;
    }
    
    public static SyncObject<BeneficiarySyncModel> readBeneficiariesSyncObject(String changes) throws JsonMappingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(changes);
        JsonNode changesNode = root.path("changes");
        
        if(!changesNode.isMissingNode()) {
            JsonNode beneficiariesNode = changesNode.path("beneficiaries");
            SyncObject<BeneficiarySyncModel> beneficiaries = mapper.treeToValue(beneficiariesNode, 
                                                                                (Class<SyncObject<BeneficiarySyncModel>>) (Object) SyncObject.class);
            return beneficiaries;
        }
        
        return null;
    }
    
    public static SyncObject<BeneficiaryInterventionSyncModel> readInterventionsSyncObject(String changes) throws JsonMappingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(changes);
        JsonNode changesNode = root.path("changes");
        
        if(!changesNode.isMissingNode()) {
            JsonNode beneficiaries_interventionsNode = changesNode.path("beneficiaries_interventions");
            if(!beneficiaries_interventionsNode.isMissingNode()) {
            	SyncObject<BeneficiaryInterventionSyncModel> beneficiaries_interventions = mapper.treeToValue(beneficiaries_interventionsNode, 
                        (Class<SyncObject<BeneficiaryInterventionSyncModel>>) (Object) SyncObject.class);
            	return beneficiaries_interventions;
            }
            
            
        }
        
        return null;
    }
    
    public static SyncObject<BeneficiaryVulnerabilitySyncModel> readVulnerabilitiesSyncObject(String changes) throws JsonMappingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(changes);
        JsonNode changesNode = root.path("changes");
        
        if(!changesNode.isMissingNode()) {
            JsonNode vulnerabilitiesNode = changesNode.path("vulnerabilities");
            SyncObject<BeneficiaryVulnerabilitySyncModel> vulnerabilities = mapper.treeToValue(vulnerabilitiesNode, 
                                                                                (Class<SyncObject<BeneficiaryVulnerabilitySyncModel>>) (Object) SyncObject.class);
            return vulnerabilities;
        }
        
        return null;
    }

    public static String readLastPulledAt(String changes) throws JsonMappingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(changes);
        JsonNode lastPulledAtNode = root.path("lastPulledAt");

        if (!lastPulledAtNode.isMissingNode()) {
            return mapper.treeToValue(lastPulledAtNode, String.class);
        }
        return null;
    }
}

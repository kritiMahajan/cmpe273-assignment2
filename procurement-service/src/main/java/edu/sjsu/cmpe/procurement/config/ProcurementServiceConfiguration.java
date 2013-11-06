package edu.sjsu.cmpe.procurement.config;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yammer.dropwizard.config.Configuration;

public class ProcurementServiceConfiguration extends Configuration {
    @NotEmpty
    @JsonProperty
    private String stompQueueName;

    @NotEmpty
    @JsonProperty
    private String stompTopicNameA;
    
    @NotEmpty
    @JsonProperty
    private String stompTopicNameB;
    
    @NotEmpty
    @JsonProperty
    private String apolloUser;
    
    @NotEmpty
    @JsonProperty
    private String apolloPassword; 
    
    @NotEmpty
    @JsonProperty
    private String apolloHost; 
    
    @NotEmpty
    @JsonProperty
    private String apolloPort;
    

    
    
    /**
     * @return the stompQueueName
     */
    public String getStompQueueName() {
	return stompQueueName;
    }

    /**
     * @param stompQueueName
     *            the stompQueueName to set
     */
    public void setStompQueueName(String stompQueueName) {
	this.stompQueueName = stompQueueName;
    }

	public String getStompTopicNameA() {
		return stompTopicNameA;
	}

	public void setStompTopicNameA(String stompTopicNameA) {
		this.stompTopicNameA = stompTopicNameA;
	}

	public String getStompTopicNameB() {
		return stompTopicNameB;
	}

	public void setStompTopicNameB(String stompTopicNameB) {
		this.stompTopicNameB = stompTopicNameB;
	}

	public String getApolloUser() {
		return apolloUser;
	}

	public void setApolloUser(String apolloUser) {
		this.apolloUser = apolloUser;
	}

	public String getApolloPassword() {
		return apolloPassword;
	}

	public void setApolloPassword(String apolloPassword) {
		this.apolloPassword = apolloPassword;
	}

	public String getApolloHost() {
		return apolloHost;
	}

	public void setApolloHost(String apolloHost) {
		this.apolloHost = apolloHost;
	}

	public String getApolloPort() {
		return apolloPort;
	}

	public void setApolloPort(String apolloPort) {
		this.apolloPort = apolloPort;
	}

	
}

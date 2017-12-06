package com.em248.bosh.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * Created by geekyzk on 05/12/2017.
 */
@Data
public class Release {

    private String name;

    @JsonProperty("release_versions")
    private List<ReleaseVersion> releaseVersions;


    @Data
    static class ReleaseVersion {
        private String version;

        @JsonProperty("commit_hash")
        private String commitHash;

        @JsonProperty("uncommitted_changes")
        private Boolean uncommittedChanges;

        @JsonProperty("currently_deployed")
        private Boolean currentlyDeployed;

        @JsonProperty("job_names")
        private List<String> jobName;
    }

}

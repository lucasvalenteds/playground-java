package com.playground.java.gitlab4j;

import org.gitlab4j.api.Constants;
import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.Issue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Collectors;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;

import static org.assertj.core.api.Assertions.assertThat;

@EnabledIfEnvironmentVariable(named = "GITLAB_TOKEN", matches = "/.*\\S.*/")
class MainTest {

    private static Properties properties;
    private static GitLabApi client;

    @BeforeAll
    static void setUpTestSuite() {
        try (var stream = new FileInputStream(new File("local.properties"))) {
            properties = new Properties();
            properties.load(stream);

            var hostUrl = Optional.ofNullable(properties.getProperty("gitlab.url"))
                    .or(() -> Optional.ofNullable(System.getenv("GITLAB_URL")))
                    .orElse("");

            var privateToken = Optional.ofNullable(properties.getProperty("gitlab.token"))
                    .or(() -> Optional.ofNullable(System.getenv("GITLAB_TOKEN")))
                    .orElse("");

            client = new GitLabApi(hostUrl, privateToken);
        } catch (IOException exception) {
            properties = new Properties();
            client = new GitLabApi("", "");
        }
    }

    @DisplayName("It supports login via access token")
    @Test
    void testLoginAccessToken() {
        assertThat(client.getTokenType()).isEqualTo(Constants.TokenType.PRIVATE);
        assertThat(client.getAuthToken()).isNotEqualTo("");
    }

    @DisplayName("It provides only synchronous API clients")
    @Test
    void testGettingAccountInformation() throws GitLabApiException {
        var accountId = Optional.ofNullable(properties.getProperty("gitlab.account"))
                .or(() -> Optional.ofNullable(System.getenv("GITLAB_ACCOUNT")))
                .map(Integer::parseInt)
                .orElse(0);

        var account = client.getUserApi().getUser(accountId);

        assertThat(account.getUsername()).isNotEmpty();
        assertThat(account.getId()).isNotEqualTo(0);
    }

    @DisplayName("The Pager Stream can be consumed lazily")
    @Test
    void testPagerStreamLazy() throws GitLabApiException {
        var api = client.getIssuesApi();

        var titles = api.getIssues(10).lazyStream()
                .filter(issue -> issue.getState() == Constants.IssueState.OPENED)
                .map(Issue::getTitle)
                .limit(5)
                .collect(Collectors.toList());

        assertThat(titles.size()).isEqualTo(5);
        assertThat(titles).doesNotContain("");
    }

    @DisplayName("The Pager Stream can be consumed eagerly")
    @Test
    void testPagerStreamPreFetched() throws GitLabApiException {
        var api = client.getIssuesApi();

        var titles = api.getIssuesStream()
                .map(Issue::getTitle)
                .limit(5)
                .collect(Collectors.toList());

        assertThat(titles.size()).isEqualTo(5);
        assertThat(titles).doesNotContain("");
    }
}

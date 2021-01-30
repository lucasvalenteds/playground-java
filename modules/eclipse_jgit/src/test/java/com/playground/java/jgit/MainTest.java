package com.playground.java.jgit;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class MainTest {

    private static final File PROJECT_DIR = Paths.get(System.getProperty("user.dir"))
        .resolve("../").resolve("../").resolve(".git/")
        .normalize()
        .toFile();

    @DisplayName("Getting all commits from HEAD")
    @Test
    void testLogCommits() throws IOException, GitAPIException {
        System.out.println(PROJECT_DIR.toString());
        Repository repository = new FileRepositoryBuilder()
            .findGitDir(PROJECT_DIR)
            .setMustExist(true)
            .build();

        Iterable<RevCommit> commits = new Git(repository).log().call();

        assertThat(commits)
            .extracting(RevCommit::getFullMessage)
            .hasSizeGreaterThan(1)
            .doesNotContainNull();
    }

    @DisplayName("Getting files from latest commit")
    @Test
    void testListingFiles() throws IOException {
        Repository repository = new FileRepositoryBuilder()
            .findGitDir(PROJECT_DIR)
            .setMustExist(true)
            .build();

        Ref head = repository.findRef("HEAD");
        RevWalk walk = new RevWalk(repository);
        RevCommit commit = walk.parseCommit(head.getObjectId());

        TreeWalk treeWalk = new TreeWalk(repository);
        treeWalk.addTree(commit.getTree());
        treeWalk.setRecursive(false);

        List<String> files = new ArrayList<>();
        while (treeWalk.next()) {
            files.add(treeWalk.getPathString());
        }

        assertThat(files).contains("README.md", "pom.xml");
    }
}

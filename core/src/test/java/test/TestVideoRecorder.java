package test;

import com.automation.remarks.video.recorder.VideoRecorder;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import static com.automation.remarks.video.recorder.VideoRecorder.conf;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * Created by sergey on 5/3/16.
 */
public class TestVideoRecorder {

    private static final String VIDEO_FILE_NAME = "video_test";
    private static final String VIDEO_FOLDER_NAME = "video";

    private LinkedList<File> recordVideo() {
        VideoRecorder recorder = new VideoRecorder(VIDEO_FILE_NAME);
        recorder.start();
        return recorder.stop();
    }

    @Before
    public void setUp() throws IOException {
        FileUtils.deleteDirectory(conf().getVideoFolder());
    }

    @After
    public void tearDown() throws Exception {
        setUp();
    }

    @Test
    public void shouldBeListWithOneVideo() {
        LinkedList<File> files = recordVideo();
        assertEquals(files.size(), 1);
    }

    @Test
    public void shouldBeVideoInRecordingsFolder() throws IOException {
        conf().withVideoFolder(VIDEO_FOLDER_NAME);
        File video = recordVideo().getFirst();
        String folderName = video.getParentFile().getName();
        assertEquals(folderName, VIDEO_FOLDER_NAME);
    }

    @Test
    public void shouldBeAbsoluteRecordingPath() throws Exception {
        recordVideo();
        String lastRecordingPath = VideoRecorder.getLastRecordingPath();
        assertThat(lastRecordingPath, startsWith(conf().getVideoFolder().getAbsolutePath() + File.separator + VIDEO_FILE_NAME));
    }

    @Test
    public void shouldBeExactVideoFileName() throws Exception {
        String fileName = recordVideo().getFirst().getName();
        assertThat(fileName, startsWith(VIDEO_FILE_NAME));
    }

    @Test
    public void shouldBeEmptyListIfRecordingWasNotStarted() throws Exception {
        VideoRecorder recorder = new VideoRecorder(VIDEO_FILE_NAME);
        LinkedList<File> stop = recorder.stop();
        assertEquals(0, stop.size());
    }
}
package su.hotty.editor.dto;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class FileListResponse {

    private List<String> files;

    public FileListResponse(List<String> files) {
        this.files = files;
    }

    public FileListResponse(String file) {
        this(Lists.newArrayList(file));
    }

    public FileListResponse() {
        this(new ArrayList<>());
    }

    public List<String> getFiles() {
        return files;
    }

    public void setFiles(List<String> files) {
        this.files = files;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FileListResponse that = (FileListResponse) o;

        return files.equals(that.files);
    }

    @Override
    public int hashCode() {
        return files.hashCode();
    }

    @Override
    public String toString() {
        return "FileListResponse{" +
                "files=" + files +
                '}';
    }
}

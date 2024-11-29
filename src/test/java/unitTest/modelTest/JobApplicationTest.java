package unitTest.modelTest;

import model.JobApplication;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.Date;


import static org.junit.jupiter.api.Assertions.*;

class JobApplicationTest {

    private JobApplication jobApplication;

    @BeforeEach
    void setUp() {
        jobApplication = new JobApplication(); // Default constructor
    }
    @Test
    void testDefaultConstructor() {
        assertEquals(-1, jobApplication.getId());
        assertNull(jobApplication.getPosition());
        assertNull(jobApplication.getCompanyName());
        assertEquals(0, jobApplication.getSalaryApproximation());
        assertNull(jobApplication.getLocation());
        assertNull(jobApplication.getStatus());
        assertNull(jobApplication.getDateSaved());
        assertNull(jobApplication.getDeadline());
        assertNull(jobApplication.getDateApplied());
        assertNull(jobApplication.getFollowUpDate());
        assertEquals(0, jobApplication.getExcitement());
        assertEquals(0, jobApplication.getUserId());
    }
    @Test
    void testConstructorWithDateSaved() {
        Date now = new Date(System.currentTimeMillis());
        JobApplication job = new JobApplication("Position", "Company", 50000, "Location", "Applied", now, now, now, now, 3, 1);

        assertEquals("Position", job.getPosition());
        assertEquals("Company", job.getCompanyName());
        assertEquals(50000, job.getSalaryApproximation());
        assertEquals("Location", job.getLocation());
        assertEquals("Applied", job.getStatus());
        assertEquals(now, job.getDateSaved());
        assertEquals(now, job.getDeadline());
        assertEquals(now, job.getDateApplied());
        assertEquals(now, job.getFollowUpDate());
        assertEquals(3, job.getExcitement());
        assertEquals(1, job.getUserId());
    }
    @Test
    void testConstructorWithoutDateSaved() {
        Date now = new Date(System.currentTimeMillis());
        JobApplication job = new JobApplication("Position", "Company", 60000, "Location", "Interview", now, now, now, 4, 2);

        assertEquals("Position", job.getPosition());
        assertEquals("Company", job.getCompanyName());
        assertEquals(60000, job.getSalaryApproximation());
        assertEquals("Location", job.getLocation());
        assertEquals("Interview", job.getStatus());
        assertNotNull(job.getDateSaved()); // Ensure dateSaved is set to the current date
        assertEquals(now, job.getDeadline());
        assertEquals(now, job.getDateApplied());
        assertEquals(now, job.getFollowUpDate());
        assertEquals(4, job.getExcitement());
        assertEquals(2, job.getUserId());
    }
    @Test
    void testGettersAndSetters() {
        jobApplication.setId(10);
        assertEquals(10, jobApplication.getId());

        jobApplication.setPosition("Developer");
        assertEquals("Developer", jobApplication.getPosition());

        jobApplication.setCompanyName("TechCorp");
        assertEquals("TechCorp", jobApplication.getCompanyName());

        jobApplication.setSalaryApproximation(70000);
        assertEquals(70000, jobApplication.getSalaryApproximation());

        jobApplication.setLocation("Remote");
        assertEquals("Remote", jobApplication.getLocation());

        jobApplication.setStatus("Offer");
        assertEquals("Offer", jobApplication.getStatus());

        Date now = new Date(System.currentTimeMillis());
        jobApplication.setDateSaved(now);
        assertEquals(now, jobApplication.getDateSaved());

        jobApplication.setDeadline(now);
        assertEquals(now, jobApplication.getDeadline());

        jobApplication.setDateApplied(now);
        assertEquals(now, jobApplication.getDateApplied());

        jobApplication.setFollowUpDate(now);
        assertEquals(now, jobApplication.getFollowUpDate());

        jobApplication.setExcitement(5);
        assertEquals(5, jobApplication.getExcitement());

        jobApplication.setUserId(1);
        assertEquals(1, jobApplication.getUserId());
    }
    @Test
    void testSetExcitementValid() {
        jobApplication.setExcitement(0);
        assertEquals(0, jobApplication.getExcitement());

        jobApplication.setExcitement(3);
        assertEquals(3, jobApplication.getExcitement());

        jobApplication.setExcitement(5);
        assertEquals(5, jobApplication.getExcitement());
    }
    @Test
    void testSetExcitementInvalid() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> jobApplication.setExcitement(-1));
        assertEquals("Excitement rating must be between 0 and 5.", exception.getMessage());

        exception = assertThrows(IllegalArgumentException.class, () -> jobApplication.setExcitement(6));
        assertEquals("Excitement rating must be between 0 and 5.", exception.getMessage());
    }
}
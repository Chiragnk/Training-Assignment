import com.monetize360.contactbook.service.DatabaseReaderUtil;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class DatabaseConnectionTest {
    @Test
    void testGetConnection() {
        assertDoesNotThrow(() -> {
            try (Connection connection = DatabaseReaderUtil.getConnection()) {
                assertNotNull(connection);
                assertTrue(connection.isValid(2));
            }
        });
    }

}

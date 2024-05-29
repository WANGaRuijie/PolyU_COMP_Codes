package instructionTest.AdministratorTest;

import database.Database;
import model.instruction.administrator.AdministratorEditProductExecutor;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import static org.mockito.Mockito.*;

public class AdministratorEditProductExecutorTest {

    private AdministratorEditProductExecutor executor;

    @Mock
    private Database mockedDatabase;

    @Mock
    private ResultSet mockedResultSet;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        executor = new AdministratorEditProductExecutor("1234567890");
    }

    @Test
    public void testIsColumnName_ValidColumnName_ReturnsTrue() throws SQLException {
        when(mockedResultSet.getMetaData()).thenReturn(mock(ResultSetMetaData.class));
        when(mockedResultSet.getMetaData().getColumnCount()).thenReturn(1);
        when(mockedResultSet.getMetaData().getColumnName(1)).thenReturn("COLUMN_NAME");
        boolean isColumnName = executor.isColumnName("COLUMN_NAME");
        Assert.assertTrue(isColumnName);
    }

    @Test
    public void testIsColumnName_InvalidColumnName_ReturnsFalse() throws SQLException {
        when(mockedResultSet.getMetaData()).thenReturn(mock(ResultSetMetaData.class));
        when(mockedResultSet.getMetaData().getColumnCount()).thenReturn(1);
        when(mockedResultSet.getMetaData().getColumnName(1)).thenReturn("COLUMN_NAME");
        boolean isColumnName = executor.isColumnName("INVALID_COLUMN_NAME");
        Assert.assertFalse(isColumnName);
    }

    @Test
    public void testIsValidNewValue_ValidProductID_ReturnsTrue() throws SQLException {
        when(mockedResultSet.getMetaData()).thenReturn(mock(ResultSetMetaData.class));
        when(mockedResultSet.getMetaData().getColumnCount()).thenReturn(1);
        when(mockedResultSet.getMetaData().getColumnName(1)).thenReturn("PRODUCT_ID");
        boolean isValidNewValue = executor.isValidNewValue("123");
        Assert.assertTrue(isValidNewValue);
    }

    @Test
    public void testIsValidNewValue_InvalidProductID_ReturnsFalse() throws SQLException {
        when(mockedResultSet.getMetaData()).thenReturn(mock(ResultSetMetaData.class));
        when(mockedResultSet.getMetaData().getColumnCount()).thenReturn(1);
        when(mockedResultSet.getMetaData().getColumnName(1)).thenReturn("PRODUCT_ID");
        boolean isValidNewValue = executor.isValidNewValue("INVALID_PRODUCT_ID");
        Assert.assertFalse(isValidNewValue);
    }

    @Test
    public void testVisitDatabase_UpdatesProductInDatabase() throws SQLException {
        String sql = "UPDATE PRODUCT SET PROPERTY_NAME = 'NEW_VALUE' WHERE PRODUCT_ID = '123'";
        executor.propertyName = "PROPERTY_NAME";
        executor.newValue = "NEW_VALUE";
        executor.productID = "123";
        executor.visitDatabase();
        verify(mockedDatabase).update(sql);
    }

}

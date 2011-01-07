package timmons.android.map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

public class MapDBAdapter {

	 
	static final String DBNAME="demoDB";
	
	/** Employees Table Definition. */
	static final String employeeTable="Employees";
	static final String colID="EmployeeID";
	static final String colName="EmployeeName";
	
	 /** Credentials Table Definition. */
	static final String loginTable="LoginInfo";
	static final String colLoginID="EmployeeID";
	static final String colUserName="UserName";
	static final String colPassword="Password";
	
	static final String viewEmps="ViewEmps";
	
	private static final String LOGIN_CREATE="CREATE TABLE "+loginTable+" ("+colLoginID+ " INTEGER PRIMARY KEY , "+
				 colUserName+ " TEXT, " + colPassword + " TEXT);";
	
	private static final String EMPLOYEE_CREATE="CREATE TABLE "+employeeTable+" ("+colID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
    colName+" TEXT ,FOREIGN KEY ("+colID+") REFERENCES "+loginTable+" ("+loginTable+"));";
	
	private static final String EMPLOYEE_VIEW_CREATE="CREATE VIEW "+viewEmps+
    " AS SELECT "+employeeTable+"."+colID+" AS _id,"+
    " "+employeeTable+"."+colName+","+
    " "+loginTable+"."+colUserName+""+
    " "+loginTable+"."+colPassword+""+
    " FROM "+employeeTable+" JOIN "+loginTable+" ON "+employeeTable+"."+colID+" ="+loginTable+"."+colLoginID;
	
	// Variable to hold the database instance
	private SQLiteDatabase db;
	
	// Context of the application using the database.
	private final Context context;
	
	private DatabaseHelper dbHelper;
	
	public MapDBAdapter(Context _context)
	{
		context=_context;
		dbHelper=new DatabaseHelper(context,DBNAME,null,33);
	}
	
	public MapDBAdapter open() throws SQLException
	{
		db=dbHelper.getWritableDatabase();
		return this;
	}
	
	public void close()
	{
		db.close();
	}
	
	public void insertData()
	{
		insertEmployees();
		insertLoginInfos();
		
	}
	
	private void insertEmployees()
	{		
		ContentValues cv=new ContentValues();
		   cv.put(colID, 1);
		   cv.put(colName, "Test");
		   db.insert(employeeTable, colID, cv);
		   		  
		   cv.put(colID, 2);
		   cv.put(colName, "Apurva");
		   db.insert(employeeTable, colID, cv);
		   		   
		   cv.put(colID, 3);
		   cv.put(colName, "Timmons");
		   db.insert(employeeTable, colID, cv);
		   				
	}
	
	private void insertLoginInfos()
	{
		//TODO: add triggers for FK constraint
		ContentValues cv=new ContentValues();
		   cv.put(colLoginID, 1);
		   cv.put(colUserName, "testuser");
		   cv.put(colPassword, "test");
		   db.insert(loginTable, colLoginID, cv);
		   		  
		   cv.put(colLoginID, 2);
		   cv.put(colUserName, "apurva.goyal");
		   cv.put(colPassword, "tg");
		   db.insert(loginTable, colLoginID, cv);
		   		   
		   cv.put(colLoginID, 3);
		   cv.put(colUserName, "timmons");
		   cv.put(colPassword, "tg");
		   db.insert(loginTable, colLoginID, cv);
	}
	
	public boolean removeEmployee(long _rowIndex) {
		return (db.delete(employeeTable, colID +
		"=" + _rowIndex, null)+  db.delete(loginTable, colLoginID +
				"=" + _rowIndex, null))> 0;
		}
	/**
	public Cursor getAllEntries () {
		return db.query(DATABASE_TABLE, new String[] {KEY_ID, KEY_NAME},
		null, null, null, null, null);
		}
		
		public int updateEntry(long _rowIndex, MyObject _myObject) {
		String where = KEY_ID + “=” + _rowIndex;
		ContentValues contentValues = new ContentValues();
		// TODO fill in the ContentValue based on the new object
		return db.update(DATABASE_TABLE, contentValues, where, null);
		}
		
		**/
	
	public Employee getEmployee(String username, String password) {
		String [] columns=new String[]{colID,colName};
		String where = colUserName + "=" + username +" AND " + colPassword +"=" +password;
		Cursor c=db.query(viewEmps, columns, where, columns, null, null, null);
			  
		Employee employee = new Employee(c.getInt(0),c.getString(1));
		// TODO Return a cursor to a row from the database and
		// use the values to populate an instance of MyObject
		return employee;
		}
	
	
	
	private static class DatabaseHelper extends SQLiteOpenHelper{
						
		public DatabaseHelper(Context context, String name,
				CursorFactory factory, int version) {
			  super(context, name, factory,version); 
			  }
		
		@Override
		public void onCreate(SQLiteDatabase _db) {
			 _db.execSQL(LOGIN_CREATE);
			 _db.execSQL(EMPLOYEE_CREATE);
			 _db.execSQL(EMPLOYEE_VIEW_CREATE); 
		}


		@Override
		public void onUpgrade(SQLiteDatabase _db, int _oldVersion, int _newVersion) {
			// TODO Auto-generated method stub
			// Log the version upgrade.
			Log.w("TaskDBAdapter", "Upgrading from version " +
			_oldVersion + " to " +
			_newVersion +
			", which will destroy all old data");
			_db.execSQL("DROP TABLE IF EXISTS "+employeeTable);
			  _db.execSQL("DROP TABLE IF EXISTS "+loginTable);  
			  _db.execSQL("DROP VIEW IF EXISTS "+viewEmps);
			  onCreate(_db);
		}
	}
}

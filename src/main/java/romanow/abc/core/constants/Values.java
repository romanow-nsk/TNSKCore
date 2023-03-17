package romanow.abc.core.constants;

import romanow.abc.core.UniException;
import romanow.abc.core.entity.*;
import romanow.abc.core.entity.base.WorkSettingsBase;
import romanow.abc.core.entity.subjectarea.*;
import romanow.abc.core.entity.users.User;

import java.util.HashMap;


public class Values extends ValuesBase {
    private static Values two=null;
    private Values(){
        super();
        System.out.println("Инициализация Values");
        }
    public final static Values init(){
        if (two == null){
            two = new Values();
            two.afterInit();
            setOne(two);
            }
        return two;
        }

    // 1. Константы наследуются (аннотации)
    // 2. Массивы строк перекрываются
    // 3. статическая инициализация наследуется
    private final static int EMReleaseNumber=1;                  // номер сборки сервера
    private User superUser = new User(UserSuperAdminType, "Система", "", "", "TNskDataserver", "pi31415926","9130000000");
    //-----------------------------------------------------------------------------
    public final static int PopupMessageDelay=6;                // Тайм-аут всплывающего окна
    public final static int PopupLongDelay=20;                  // Тайм-аут всплывающего окна
    private  final static String EMClassNames[]={
            "romanow.abc.core.constants.Values",
            "romanow.abc.core.entity.WorkSettings",
            "romanow.abc.dataserver.TNskDataServer",
            "romanow.abc.dataserver.TNskConsoleServer",
            "romanow.abc.desktop.Cabinet",
            "romanow.abc.desktop.Client",
            "","",""};
    private  final static String EMAppNames[]={
            "tnsk",
            "tnsk",
            "tnsk",
            "tnsk",
            "TNsk.apk",
            "TNskDataserver.jar",
            "romanow.abc.desktop.module",
            "/drawable/busstop.png",
            "Транспорт-NSK"
            };
    @Override
    protected void initEnv() {
        super.initEnv();
        I_Environment env = new I_Environment() {
            @Override
            public String applicationClassName(int classType) {
                return EMClassNames[classType];
            }

            @Override
            public String applicationName(int nameNype) {
                return EMAppNames[nameNype];
            }

            @Override
            public User superUser() {
                return superUser;
            }

            @Override
            public Class applicationClass(int classType) throws UniException {
                return createApplicationClass(classType, EMClassNames);
            }

            @Override
            public Object applicationObject(int classType) throws UniException {
                return createApplicationObject(classType, EMClassNames);
            }

            @Override
            public int releaseNumber() {
                return EMReleaseNumber;
            }

            @Override
            public WorkSettingsBase currentWorkSettings() {
                return new WorkSettings();
            }
        };
        setEnv(env);
        }
    @Override
    protected void initTables(){
        super.initTables();
        EntityIndexedFactory EntityFactory = getEntityFactory();
        EntityFactory.put(new TableItem("Точка сегмента", TSegPoint.class));
        EntityFactory.put(new TableItem("Cегмент", TSegment.class));
        EntityFactory.put(new TableItem("Остановка", TStop.class));
        EntityFactory.put(new TableItem("Маршрут", TRoute.class));
        EntityFactory.put(new TableItem("Сегмент маршрута", TRouteSegment.class));
        EntityFactory.put(new TableItem("Остановка маршрута", TRouteStop.class));
        EntityFactory.put(new TableItem("Настройки", WorkSettings.class));
        HashMap<String,String> PrefixMap = getPrefixMap();
        PrefixMap.put("TSegPoint.gps","g");
        PrefixMap.put("TStop.gps","g");
        /*
        EntityFactory.put(new TableItem("Мета:Внешняя подсистема", MetaExternalSystem.class));
        EntityFactory.put(new TableItem("Мета:Подсистемы", MetaSubSystem.class));
        EntityFactory.put(new TableItem("Мета:Состояние", MetaState.class).add("MetaExternalSystem"));
         */
       }
    //------------- Типы пользователей -----------------------------------------------------
    @CONST(group = "User", title = "Тьютор")
    public final static int UserTutor = 3;
    @CONST(group = "User", title = "Преподаватель")
    public final static int UserTeacher = 4;
    @CONST(group = "User", title = "Студент")
    public final static int UserStudent = 5;
    //----------------------- Отчеты  ---------------------------------------------
    @CONST(group = "Report", title = "Рейтинг группы")
    public final static int RepGroup = 1;
    @CONST(group = "Report", title = "Рейтинг студента")
    public final static int RepStudent = 2;
    @CONST(group = "Report", title = "Рейтинг уч.единицы")
    public final static int RepEduUnit = 3;
    //-------------------------------------------------------------------------------------------
    public static void main(String a[]){
        Values.init();
        System.out.println(title("User", UserAdminType));
        System.out.print(Values.constMap().toString());
        }
}

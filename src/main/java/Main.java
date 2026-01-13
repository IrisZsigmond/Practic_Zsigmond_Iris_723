import controller.ConsoleController;
import service.EreignisService;
import service.FahrerService;
import service.StrafeService;

public class Main {
    public static void main(String[] args) {
        var controller = new ConsoleController(
                new StrafeService(),
                new EreignisService(),
                new FahrerService()
        );
        controller.run();
    }
}

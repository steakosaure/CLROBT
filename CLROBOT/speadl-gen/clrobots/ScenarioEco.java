package clrobots;

import clrobots.Environnement;
import clrobots.GUI;
import clrobots.Launcher;
import clrobots.RobotForwardAssemblyEco;
import clrobots.interfaces.CycleAlert;
import clrobots.interfaces.ICreateRobot;
import clrobots.interfaces.ITakeThreads;

@SuppressWarnings("all")
public abstract class ScenarioEco<Actionable, Context, ContextInit, SelfKnowledge, UpdateOutput> {
  public interface Requires<Actionable, Context, ContextInit, SelfKnowledge, UpdateOutput> {
  }
  
  public interface Component<Actionable, Context, ContextInit, SelfKnowledge, UpdateOutput> extends ScenarioEco.Provides<Actionable, Context, ContextInit, SelfKnowledge, UpdateOutput> {
  }
  
  public interface Provides<Actionable, Context, ContextInit, SelfKnowledge, UpdateOutput> {
  }
  
  public interface Parts<Actionable, Context, ContextInit, SelfKnowledge, UpdateOutput> {
    /**
     * This can be called by the implementation to access the part and its provided ports.
     * It will be initialized after the required ports are initialized and before the provided ports are initialized.
     * 
     */
    public Environnement.Component<Actionable, Context, ContextInit, UpdateOutput> environnement();
    
    /**
     * This can be called by the implementation to access the part and its provided ports.
     * It will be initialized after the required ports are initialized and before the provided ports are initialized.
     * 
     */
    public GUI.Component<UpdateOutput, ContextInit> gui();
    
    /**
     * This can be called by the implementation to access the part and its provided ports.
     * It will be initialized after the required ports are initialized and before the provided ports are initialized.
     * 
     */
    public RobotForwardAssemblyEco.Component<Actionable, Context, SelfKnowledge> rfAssemblyEco();
    
    /**
     * This can be called by the implementation to access the part and its provided ports.
     * It will be initialized after the required ports are initialized and before the provided ports are initialized.
     * 
     */
    public Launcher.Component launcher();
  }
  
  public static class ComponentImpl<Actionable, Context, ContextInit, SelfKnowledge, UpdateOutput> implements ScenarioEco.Component<Actionable, Context, ContextInit, SelfKnowledge, UpdateOutput>, ScenarioEco.Parts<Actionable, Context, ContextInit, SelfKnowledge, UpdateOutput> {
    private final ScenarioEco.Requires<Actionable, Context, ContextInit, SelfKnowledge, UpdateOutput> bridge;
    
    private final ScenarioEco<Actionable, Context, ContextInit, SelfKnowledge, UpdateOutput> implementation;
    
    public void start() {
      assert this.environnement != null: "This is a bug.";
      ((Environnement.ComponentImpl<Actionable, Context, ContextInit, UpdateOutput>) this.environnement).start();
      assert this.gui != null: "This is a bug.";
      ((GUI.ComponentImpl<UpdateOutput, ContextInit>) this.gui).start();
      assert this.rfAssemblyEco != null: "This is a bug.";
      ((RobotForwardAssemblyEco.ComponentImpl<Actionable, Context, SelfKnowledge>) this.rfAssemblyEco).start();
      assert this.launcher != null: "This is a bug.";
      ((Launcher.ComponentImpl) this.launcher).start();
      this.implementation.start();
      this.implementation.started = true;
    }
    
    private void init_environnement() {
      assert this.environnement == null: "This is a bug.";
      assert this.implem_environnement == null: "This is a bug.";
      this.implem_environnement = this.implementation.make_environnement();
      if (this.implem_environnement == null) {
      	throw new RuntimeException("make_environnement() in clrobots.ScenarioEco<Actionable, Context, ContextInit, SelfKnowledge, UpdateOutput> should not return null.");
      }
      this.environnement = this.implem_environnement._newComponent(new BridgeImpl_environnement(), false);
      
    }
    
    private void init_gui() {
      assert this.gui == null: "This is a bug.";
      assert this.implem_gui == null: "This is a bug.";
      this.implem_gui = this.implementation.make_gui();
      if (this.implem_gui == null) {
      	throw new RuntimeException("make_gui() in clrobots.ScenarioEco<Actionable, Context, ContextInit, SelfKnowledge, UpdateOutput> should not return null.");
      }
      this.gui = this.implem_gui._newComponent(new BridgeImpl_gui(), false);
      
    }
    
    private void init_rfAssemblyEco() {
      assert this.rfAssemblyEco == null: "This is a bug.";
      assert this.implem_rfAssemblyEco == null: "This is a bug.";
      this.implem_rfAssemblyEco = this.implementation.make_rfAssemblyEco();
      if (this.implem_rfAssemblyEco == null) {
      	throw new RuntimeException("make_rfAssemblyEco() in clrobots.ScenarioEco<Actionable, Context, ContextInit, SelfKnowledge, UpdateOutput> should not return null.");
      }
      this.rfAssemblyEco = this.implem_rfAssemblyEco._newComponent(new BridgeImpl_rfAssemblyEco(), false);
      
    }
    
    private void init_launcher() {
      assert this.launcher == null: "This is a bug.";
      assert this.implem_launcher == null: "This is a bug.";
      this.implem_launcher = this.implementation.make_launcher();
      if (this.implem_launcher == null) {
      	throw new RuntimeException("make_launcher() in clrobots.ScenarioEco<Actionable, Context, ContextInit, SelfKnowledge, UpdateOutput> should not return null.");
      }
      this.launcher = this.implem_launcher._newComponent(new BridgeImpl_launcher(), false);
      
    }
    
    protected void initParts() {
      init_environnement();
      init_gui();
      init_rfAssemblyEco();
      init_launcher();
    }
    
    protected void initProvidedPorts() {
      
    }
    
    public ComponentImpl(final ScenarioEco<Actionable, Context, ContextInit, SelfKnowledge, UpdateOutput> implem, final ScenarioEco.Requires<Actionable, Context, ContextInit, SelfKnowledge, UpdateOutput> b, final boolean doInits) {
      this.bridge = b;
      this.implementation = implem;
      
      assert implem.selfComponent == null: "This is a bug.";
      implem.selfComponent = this;
      
      // prevent them to be called twice if we are in
      // a specialized component: only the last of the
      // hierarchy will call them after everything is initialised
      if (doInits) {
      	initParts();
      	initProvidedPorts();
      }
    }
    
    private Environnement.Component<Actionable, Context, ContextInit, UpdateOutput> environnement;
    
    private Environnement<Actionable, Context, ContextInit, UpdateOutput> implem_environnement;
    
    private final class BridgeImpl_environnement implements Environnement.Requires<Actionable, Context, ContextInit, UpdateOutput> {
      public final UpdateOutput updateOutput() {
        return ScenarioEco.ComponentImpl.this.gui().updateGUI();
      }
      
      public final ICreateRobot createRobot() {
        return ScenarioEco.ComponentImpl.this.rfAssemblyEco().createRobot();
      }
    }
    
    public final Environnement.Component<Actionable, Context, ContextInit, UpdateOutput> environnement() {
      return this.environnement;
    }
    
    private GUI.Component<UpdateOutput, ContextInit> gui;
    
    private GUI<UpdateOutput, ContextInit> implem_gui;
    
    private final class BridgeImpl_gui implements GUI.Requires<UpdateOutput, ContextInit> {
      public final ContextInit initEnvironnement() {
        return ScenarioEco.ComponentImpl.this.environnement().envInit();
      }
    }
    
    public final GUI.Component<UpdateOutput, ContextInit> gui() {
      return this.gui;
    }
    
    private RobotForwardAssemblyEco.Component<Actionable, Context, SelfKnowledge> rfAssemblyEco;
    
    private RobotForwardAssemblyEco<Actionable, Context, SelfKnowledge> implem_rfAssemblyEco;
    
    private final class BridgeImpl_rfAssemblyEco implements RobotForwardAssemblyEco.Requires<Actionable, Context, SelfKnowledge> {
      public final ITakeThreads threads() {
        return ScenarioEco.ComponentImpl.this.launcher().threads();
      }
      
      public final CycleAlert finishedCycle() {
        return ScenarioEco.ComponentImpl.this.launcher().finishedCycle();
      }
      
      public final Context envInfos() {
        return ScenarioEco.ComponentImpl.this.environnement().envInfos();
      }
      
      public final Actionable interagir() {
        return ScenarioEco.ComponentImpl.this.environnement().interagir();
      }
    }
    
    public final RobotForwardAssemblyEco.Component<Actionable, Context, SelfKnowledge> rfAssemblyEco() {
      return this.rfAssemblyEco;
    }
    
    private Launcher.Component launcher;
    
    private Launcher implem_launcher;
    
    private final class BridgeImpl_launcher implements Launcher.Requires {
    }
    
    public final Launcher.Component launcher() {
      return this.launcher;
    }
  }
  
  /**
   * Used to check that two components are not created from the same implementation,
   * that the component has been started to call requires(), provides() and parts()
   * and that the component is not started by hand.
   * 
   */
  private boolean init = false;;
  
  /**
   * Used to check that the component is not started by hand.
   * 
   */
  private boolean started = false;;
  
  private ScenarioEco.ComponentImpl<Actionable, Context, ContextInit, SelfKnowledge, UpdateOutput> selfComponent;
  
  /**
   * Can be overridden by the implementation.
   * It will be called automatically after the component has been instantiated.
   * 
   */
  protected void start() {
    if (!this.init || this.started) {
    	throw new RuntimeException("start() should not be called by hand: to create a new component, use newComponent().");
    }
  }
  
  /**
   * This can be called by the implementation to access the provided ports.
   * 
   */
  protected ScenarioEco.Provides<Actionable, Context, ContextInit, SelfKnowledge, UpdateOutput> provides() {
    assert this.selfComponent != null: "This is a bug.";
    if (!this.init) {
    	throw new RuntimeException("provides() can't be accessed until a component has been created from this implementation, use start() instead of the constructor if provides() is needed to initialise the component.");
    }
    return this.selfComponent;
  }
  
  /**
   * This can be called by the implementation to access the required ports.
   * 
   */
  protected ScenarioEco.Requires<Actionable, Context, ContextInit, SelfKnowledge, UpdateOutput> requires() {
    assert this.selfComponent != null: "This is a bug.";
    if (!this.init) {
    	throw new RuntimeException("requires() can't be accessed until a component has been created from this implementation, use start() instead of the constructor if requires() is needed to initialise the component.");
    }
    return this.selfComponent.bridge;
  }
  
  /**
   * This can be called by the implementation to access the parts and their provided ports.
   * 
   */
  protected ScenarioEco.Parts<Actionable, Context, ContextInit, SelfKnowledge, UpdateOutput> parts() {
    assert this.selfComponent != null: "This is a bug.";
    if (!this.init) {
    	throw new RuntimeException("parts() can't be accessed until a component has been created from this implementation, use start() instead of the constructor if parts() is needed to initialise the component.");
    }
    return this.selfComponent;
  }
  
  /**
   * This should be overridden by the implementation to define how to create this sub-component.
   * This will be called once during the construction of the component to initialize this sub-component.
   * 
   */
  protected abstract Environnement<Actionable, Context, ContextInit, UpdateOutput> make_environnement();
  
  /**
   * This should be overridden by the implementation to define how to create this sub-component.
   * This will be called once during the construction of the component to initialize this sub-component.
   * 
   */
  protected abstract GUI<UpdateOutput, ContextInit> make_gui();
  
  /**
   * This should be overridden by the implementation to define how to create this sub-component.
   * This will be called once during the construction of the component to initialize this sub-component.
   * 
   */
  protected abstract RobotForwardAssemblyEco<Actionable, Context, SelfKnowledge> make_rfAssemblyEco();
  
  /**
   * This should be overridden by the implementation to define how to create this sub-component.
   * This will be called once during the construction of the component to initialize this sub-component.
   * 
   */
  protected abstract Launcher make_launcher();
  
  /**
   * Not meant to be used to manually instantiate components (except for testing).
   * 
   */
  public synchronized ScenarioEco.Component<Actionable, Context, ContextInit, SelfKnowledge, UpdateOutput> _newComponent(final ScenarioEco.Requires<Actionable, Context, ContextInit, SelfKnowledge, UpdateOutput> b, final boolean start) {
    if (this.init) {
    	throw new RuntimeException("This instance of ScenarioEco has already been used to create a component, use another one.");
    }
    this.init = true;
    ScenarioEco.ComponentImpl<Actionable, Context, ContextInit, SelfKnowledge, UpdateOutput>  _comp = new ScenarioEco.ComponentImpl<Actionable, Context, ContextInit, SelfKnowledge, UpdateOutput>(this, b, true);
    if (start) {
    	_comp.start();
    }
    return _comp;
  }
  
  /**
   * Use to instantiate a component from this implementation.
   * 
   */
  public ScenarioEco.Component<Actionable, Context, ContextInit, SelfKnowledge, UpdateOutput> newComponent() {
    return this._newComponent(new ScenarioEco.Requires<Actionable, Context, ContextInit, SelfKnowledge, UpdateOutput>() {}, true);
  }
}

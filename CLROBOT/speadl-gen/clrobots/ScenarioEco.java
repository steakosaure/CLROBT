package clrobots;

import clrobots.EcoRobot;
import clrobots.Forward;
import clrobots.Launcher;
import clrobots.interfaces.CycleAlert;
import clrobots.interfaces.ITakeThreads;
import java.awt.Color;

@SuppressWarnings("all")
public abstract class ScenarioEco {
  public interface Requires {
  }
  
  public interface Component extends ScenarioEco.Provides {
  }
  
  public interface Provides {
  }
  
  public interface Parts {
    /**
     * This can be called by the implementation to access the part and its provided ports.
     * It will be initialized after the required ports are initialized and before the provided ports are initialized.
     * 
     */
    public EcoRobot.Component ecoAE();
    
    /**
     * This can be called by the implementation to access the part and its provided ports.
     * It will be initialized after the required ports are initialized and before the provided ports are initialized.
     * 
     */
    public Forward.Component<CycleAlert> fw();
    
    /**
     * This can be called by the implementation to access the part and its provided ports.
     * It will be initialized after the required ports are initialized and before the provided ports are initialized.
     * 
     */
    public Launcher.Component launcher();
  }
  
  public static class ComponentImpl implements ScenarioEco.Component, ScenarioEco.Parts {
    private final ScenarioEco.Requires bridge;
    
    private final ScenarioEco implementation;
    
    public void start() {
      assert this.ecoAE != null: "This is a bug.";
      ((EcoRobot.ComponentImpl) this.ecoAE).start();
      assert this.fw != null: "This is a bug.";
      ((Forward.ComponentImpl<CycleAlert>) this.fw).start();
      assert this.launcher != null: "This is a bug.";
      ((Launcher.ComponentImpl) this.launcher).start();
      this.implementation.start();
      this.implementation.started = true;
    }
    
    private void init_ecoAE() {
      assert this.ecoAE == null: "This is a bug.";
      assert this.implem_ecoAE == null: "This is a bug.";
      this.implem_ecoAE = this.implementation.make_ecoAE();
      if (this.implem_ecoAE == null) {
      	throw new RuntimeException("make_ecoAE() in clrobots.ScenarioEco should not return null.");
      }
      this.ecoAE = this.implem_ecoAE._newComponent(new BridgeImpl_ecoAE(), false);
      
    }
    
    private void init_fw() {
      assert this.fw == null: "This is a bug.";
      assert this.implem_fw == null: "This is a bug.";
      this.implem_fw = this.implementation.make_fw();
      if (this.implem_fw == null) {
      	throw new RuntimeException("make_fw() in clrobots.ScenarioEco should not return null.");
      }
      this.fw = this.implem_fw._newComponent(new BridgeImpl_fw(), false);
      
    }
    
    private void init_launcher() {
      assert this.launcher == null: "This is a bug.";
      assert this.implem_launcher == null: "This is a bug.";
      this.implem_launcher = this.implementation.make_launcher();
      if (this.implem_launcher == null) {
      	throw new RuntimeException("make_launcher() in clrobots.ScenarioEco should not return null.");
      }
      this.launcher = this.implem_launcher._newComponent(new BridgeImpl_launcher(), false);
      
    }
    
    protected void initParts() {
      init_ecoAE();
      init_fw();
      init_launcher();
    }
    
    protected void initProvidedPorts() {
      
    }
    
    public ComponentImpl(final ScenarioEco implem, final ScenarioEco.Requires b, final boolean doInits) {
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
    
    private EcoRobot.Component ecoAE;
    
    private EcoRobot implem_ecoAE;
    
    private final class BridgeImpl_ecoAE implements EcoRobot.Requires {
      public final ITakeThreads threads() {
        return ScenarioEco.ComponentImpl.this.launcher().threads();
      }
    }
    
    public final EcoRobot.Component ecoAE() {
      return this.ecoAE;
    }
    
    private Forward.Component<CycleAlert> fw;
    
    private Forward<CycleAlert> implem_fw;
    
    private final class BridgeImpl_fw implements Forward.Requires<CycleAlert> {
      public final CycleAlert i() {
        return ScenarioEco.ComponentImpl.this.launcher().finishedCycle();
      }
    }
    
    public final Forward.Component<CycleAlert> fw() {
      return this.fw;
    }
    
    private Launcher.Component launcher;
    
    private Launcher implem_launcher;
    
    private final class BridgeImpl_launcher implements Launcher.Requires {
    }
    
    public final Launcher.Component launcher() {
      return this.launcher;
    }
  }
  
  public static class DynamicAssembly {
    public interface Requires {
    }
    
    public interface Component extends ScenarioEco.DynamicAssembly.Provides {
    }
    
    public interface Provides {
    }
    
    public interface Parts {
      /**
       * This can be called by the implementation to access the part and its provided ports.
       * It will be initialized after the required ports are initialized and before the provided ports are initialized.
       * 
       */
      public EcoRobot.Robot.Component agentE();
      
      /**
       * This can be called by the implementation to access the part and its provided ports.
       * It will be initialized after the required ports are initialized and before the provided ports are initialized.
       * 
       */
      public Forward.Agent.Component<CycleAlert> aFW();
    }
    
    public static class ComponentImpl implements ScenarioEco.DynamicAssembly.Component, ScenarioEco.DynamicAssembly.Parts {
      private final ScenarioEco.DynamicAssembly.Requires bridge;
      
      private final ScenarioEco.DynamicAssembly implementation;
      
      public void start() {
        assert this.agentE != null: "This is a bug.";
        ((EcoRobot.Robot.ComponentImpl) this.agentE).start();
        assert this.aFW != null: "This is a bug.";
        ((Forward.Agent.ComponentImpl<CycleAlert>) this.aFW).start();
        this.implementation.start();
        this.implementation.started = true;
      }
      
      private void init_agentE() {
        assert this.agentE == null: "This is a bug.";
        assert this.implementation.use_agentE != null: "This is a bug.";
        this.agentE = this.implementation.use_agentE._newComponent(new BridgeImpl_ecoAE_agentE(), false);
        
      }
      
      private void init_aFW() {
        assert this.aFW == null: "This is a bug.";
        assert this.implementation.use_aFW != null: "This is a bug.";
        this.aFW = this.implementation.use_aFW._newComponent(new BridgeImpl_fw_aFW(), false);
        
      }
      
      protected void initParts() {
        init_agentE();
        init_aFW();
      }
      
      protected void initProvidedPorts() {
        
      }
      
      public ComponentImpl(final ScenarioEco.DynamicAssembly implem, final ScenarioEco.DynamicAssembly.Requires b, final boolean doInits) {
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
      
      private EcoRobot.Robot.Component agentE;
      
      private final class BridgeImpl_ecoAE_agentE implements EcoRobot.Robot.Requires {
        public final CycleAlert finishedCycle() {
          return ScenarioEco.DynamicAssembly.ComponentImpl.this.aFW().a();
        }
      }
      
      public final EcoRobot.Robot.Component agentE() {
        return this.agentE;
      }
      
      private Forward.Agent.Component<CycleAlert> aFW;
      
      private final class BridgeImpl_fw_aFW implements Forward.Agent.Requires<CycleAlert> {
      }
      
      public final Forward.Agent.Component<CycleAlert> aFW() {
        return this.aFW;
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
    
    private ScenarioEco.DynamicAssembly.ComponentImpl selfComponent;
    
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
    protected ScenarioEco.DynamicAssembly.Provides provides() {
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
    protected ScenarioEco.DynamicAssembly.Requires requires() {
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
    protected ScenarioEco.DynamicAssembly.Parts parts() {
      assert this.selfComponent != null: "This is a bug.";
      if (!this.init) {
      	throw new RuntimeException("parts() can't be accessed until a component has been created from this implementation, use start() instead of the constructor if parts() is needed to initialise the component.");
      }
      return this.selfComponent;
    }
    
    private EcoRobot.Robot use_agentE;
    
    private Forward.Agent<CycleAlert> use_aFW;
    
    /**
     * Not meant to be used to manually instantiate components (except for testing).
     * 
     */
    public synchronized ScenarioEco.DynamicAssembly.Component _newComponent(final ScenarioEco.DynamicAssembly.Requires b, final boolean start) {
      if (this.init) {
      	throw new RuntimeException("This instance of DynamicAssembly has already been used to create a component, use another one.");
      }
      this.init = true;
      ScenarioEco.DynamicAssembly.ComponentImpl  _comp = new ScenarioEco.DynamicAssembly.ComponentImpl(this, b, true);
      if (start) {
      	_comp.start();
      }
      return _comp;
    }
    
    private ScenarioEco.ComponentImpl ecosystemComponent;
    
    /**
     * This can be called by the species implementation to access the provided ports of its ecosystem.
     * 
     */
    protected ScenarioEco.Provides eco_provides() {
      assert this.ecosystemComponent != null: "This is a bug.";
      return this.ecosystemComponent;
    }
    
    /**
     * This can be called by the species implementation to access the required ports of its ecosystem.
     * 
     */
    protected ScenarioEco.Requires eco_requires() {
      assert this.ecosystemComponent != null: "This is a bug.";
      return this.ecosystemComponent.bridge;
    }
    
    /**
     * This can be called by the species implementation to access the parts of its ecosystem and their provided ports.
     * 
     */
    protected ScenarioEco.Parts eco_parts() {
      assert this.ecosystemComponent != null: "This is a bug.";
      return this.ecosystemComponent;
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
  
  private ScenarioEco.ComponentImpl selfComponent;
  
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
  protected ScenarioEco.Provides provides() {
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
  protected ScenarioEco.Requires requires() {
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
  protected ScenarioEco.Parts parts() {
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
  protected abstract EcoRobot make_ecoAE();
  
  /**
   * This should be overridden by the implementation to define how to create this sub-component.
   * This will be called once during the construction of the component to initialize this sub-component.
   * 
   */
  protected abstract Forward<CycleAlert> make_fw();
  
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
  public synchronized ScenarioEco.Component _newComponent(final ScenarioEco.Requires b, final boolean start) {
    if (this.init) {
    	throw new RuntimeException("This instance of ScenarioEco has already been used to create a component, use another one.");
    }
    this.init = true;
    ScenarioEco.ComponentImpl  _comp = new ScenarioEco.ComponentImpl(this, b, true);
    if (start) {
    	_comp.start();
    }
    return _comp;
  }
  
  /**
   * This should be overridden by the implementation to instantiate the implementation of the species.
   * 
   */
  protected ScenarioEco.DynamicAssembly make_DynamicAssembly(final String id, final Color color) {
    return new ScenarioEco.DynamicAssembly();
  }
  
  /**
   * Do not call, used by generated code.
   * 
   */
  public ScenarioEco.DynamicAssembly _createImplementationOfDynamicAssembly(final String id, final Color color) {
    ScenarioEco.DynamicAssembly implem = make_DynamicAssembly(id,color);
    if (implem == null) {
    	throw new RuntimeException("make_DynamicAssembly() in clrobots.ScenarioEco should not return null.");
    }
    assert implem.ecosystemComponent == null: "This is a bug.";
    assert this.selfComponent != null: "This is a bug.";
    implem.ecosystemComponent = this.selfComponent;
    assert this.selfComponent.implem_ecoAE != null: "This is a bug.";
    assert implem.use_agentE == null: "This is a bug.";
    implem.use_agentE = this.selfComponent.implem_ecoAE._createImplementationOfRobot(id,color);
    assert this.selfComponent.implem_fw != null: "This is a bug.";
    assert implem.use_aFW == null: "This is a bug.";
    implem.use_aFW = this.selfComponent.implem_fw._createImplementationOfAgent();
    return implem;
  }
  
  /**
   * This can be called to create an instance of the species from inside the implementation of the ecosystem.
   * 
   */
  protected ScenarioEco.DynamicAssembly.Component newDynamicAssembly(final String id, final Color color) {
    ScenarioEco.DynamicAssembly _implem = _createImplementationOfDynamicAssembly(id,color);
    return _implem._newComponent(new ScenarioEco.DynamicAssembly.Requires() {},true);
  }
  
  /**
   * Use to instantiate a component from this implementation.
   * 
   */
  public ScenarioEco.Component newComponent() {
    return this._newComponent(new ScenarioEco.Requires() {}, true);
  }
}

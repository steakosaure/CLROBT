package clrobots;

import clrobots.EcoRobotAgents;
import clrobots.Forward;
import clrobots.interfaces.CycleAlert;
import clrobots.interfaces.ICreateRobot;
import clrobots.interfaces.ITakeThreads;
import environnement.Cellule;
import java.awt.Color;
import java.awt.Point;
import java.util.Map;

@SuppressWarnings("all")
public abstract class RobotForwardAssemblyEco<Actionable, Context, SelfKnowledge> {
  public interface Requires<Actionable, Context, SelfKnowledge> {
    /**
     * This can be called by the implementation to access this required port.
     * 
     */
    public ITakeThreads threads();
    
    /**
     * This can be called by the implementation to access this required port.
     * 
     */
    public CycleAlert finishedCycle();
    
    /**
     * This can be called by the implementation to access this required port.
     * 
     */
    public Context envInfos();
    
    /**
     * This can be called by the implementation to access this required port.
     * 
     */
    public Actionable interagir();
  }
  
  public interface Component<Actionable, Context, SelfKnowledge> extends RobotForwardAssemblyEco.Provides<Actionable, Context, SelfKnowledge> {
  }
  
  public interface Provides<Actionable, Context, SelfKnowledge> {
    /**
     * This can be called to access the provided port.
     * 
     */
    public ICreateRobot createRobot();
  }
  
  public interface Parts<Actionable, Context, SelfKnowledge> {
    /**
     * This can be called by the implementation to access the part and its provided ports.
     * It will be initialized after the required ports are initialized and before the provided ports are initialized.
     * 
     */
    public EcoRobotAgents.Component<Actionable, Context, SelfKnowledge> ecoAE();
    
    /**
     * This can be called by the implementation to access the part and its provided ports.
     * It will be initialized after the required ports are initialized and before the provided ports are initialized.
     * 
     */
    public Forward.Component<CycleAlert, Context, Actionable> fw();
  }
  
  public static class ComponentImpl<Actionable, Context, SelfKnowledge> implements RobotForwardAssemblyEco.Component<Actionable, Context, SelfKnowledge>, RobotForwardAssemblyEco.Parts<Actionable, Context, SelfKnowledge> {
    private final RobotForwardAssemblyEco.Requires<Actionable, Context, SelfKnowledge> bridge;
    
    private final RobotForwardAssemblyEco<Actionable, Context, SelfKnowledge> implementation;
    
    public void start() {
      assert this.ecoAE != null: "This is a bug.";
      ((EcoRobotAgents.ComponentImpl<Actionable, Context, SelfKnowledge>) this.ecoAE).start();
      assert this.fw != null: "This is a bug.";
      ((Forward.ComponentImpl<CycleAlert, Context, Actionable>) this.fw).start();
      this.implementation.start();
      this.implementation.started = true;
    }
    
    private void init_ecoAE() {
      assert this.ecoAE == null: "This is a bug.";
      assert this.implem_ecoAE == null: "This is a bug.";
      this.implem_ecoAE = this.implementation.make_ecoAE();
      if (this.implem_ecoAE == null) {
      	throw new RuntimeException("make_ecoAE() in clrobots.RobotForwardAssemblyEco<Actionable, Context, SelfKnowledge> should not return null.");
      }
      this.ecoAE = this.implem_ecoAE._newComponent(new BridgeImpl_ecoAE(), false);
      
    }
    
    private void init_fw() {
      assert this.fw == null: "This is a bug.";
      assert this.implem_fw == null: "This is a bug.";
      this.implem_fw = this.implementation.make_fw();
      if (this.implem_fw == null) {
      	throw new RuntimeException("make_fw() in clrobots.RobotForwardAssemblyEco<Actionable, Context, SelfKnowledge> should not return null.");
      }
      this.fw = this.implem_fw._newComponent(new BridgeImpl_fw(), false);
      
    }
    
    protected void initParts() {
      init_ecoAE();
      init_fw();
    }
    
    private void init_createRobot() {
      assert this.createRobot == null: "This is a bug.";
      this.createRobot = this.implementation.make_createRobot();
      if (this.createRobot == null) {
      	throw new RuntimeException("make_createRobot() in clrobots.RobotForwardAssemblyEco<Actionable, Context, SelfKnowledge> should not return null.");
      }
    }
    
    protected void initProvidedPorts() {
      init_createRobot();
    }
    
    public ComponentImpl(final RobotForwardAssemblyEco<Actionable, Context, SelfKnowledge> implem, final RobotForwardAssemblyEco.Requires<Actionable, Context, SelfKnowledge> b, final boolean doInits) {
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
    
    private ICreateRobot createRobot;
    
    public ICreateRobot createRobot() {
      return this.createRobot;
    }
    
    private EcoRobotAgents.Component<Actionable, Context, SelfKnowledge> ecoAE;
    
    private EcoRobotAgents<Actionable, Context, SelfKnowledge> implem_ecoAE;
    
    private final class BridgeImpl_ecoAE implements EcoRobotAgents.Requires<Actionable, Context, SelfKnowledge> {
      public final ITakeThreads threads() {
        return RobotForwardAssemblyEco.ComponentImpl.this.bridge.threads();
      }
    }
    
    public final EcoRobotAgents.Component<Actionable, Context, SelfKnowledge> ecoAE() {
      return this.ecoAE;
    }
    
    private Forward.Component<CycleAlert, Context, Actionable> fw;
    
    private Forward<CycleAlert, Context, Actionable> implem_fw;
    
    private final class BridgeImpl_fw implements Forward.Requires<CycleAlert, Context, Actionable> {
      public final CycleAlert i() {
        return RobotForwardAssemblyEco.ComponentImpl.this.bridge.finishedCycle();
      }
      
      public final Context j() {
        return RobotForwardAssemblyEco.ComponentImpl.this.bridge.envInfos();
      }
      
      public final Actionable k() {
        return RobotForwardAssemblyEco.ComponentImpl.this.bridge.interagir();
      }
    }
    
    public final Forward.Component<CycleAlert, Context, Actionable> fw() {
      return this.fw;
    }
  }
  
  public static class DynamicAssembly<Actionable, Context, SelfKnowledge> {
    public interface Requires<Actionable, Context, SelfKnowledge> {
    }
    
    public interface Component<Actionable, Context, SelfKnowledge> extends RobotForwardAssemblyEco.DynamicAssembly.Provides<Actionable, Context, SelfKnowledge> {
    }
    
    public interface Provides<Actionable, Context, SelfKnowledge> {
    }
    
    public interface Parts<Actionable, Context, SelfKnowledge> {
      /**
       * This can be called by the implementation to access the part and its provided ports.
       * It will be initialized after the required ports are initialized and before the provided ports are initialized.
       * 
       */
      public EcoRobotAgents.Robot.Component<Actionable, Context, SelfKnowledge> agentE();
      
      /**
       * This can be called by the implementation to access the part and its provided ports.
       * It will be initialized after the required ports are initialized and before the provided ports are initialized.
       * 
       */
      public Forward.Agent.Component<CycleAlert, Context, Actionable> aFW();
    }
    
    public static class ComponentImpl<Actionable, Context, SelfKnowledge> implements RobotForwardAssemblyEco.DynamicAssembly.Component<Actionable, Context, SelfKnowledge>, RobotForwardAssemblyEco.DynamicAssembly.Parts<Actionable, Context, SelfKnowledge> {
      private final RobotForwardAssemblyEco.DynamicAssembly.Requires<Actionable, Context, SelfKnowledge> bridge;
      
      private final RobotForwardAssemblyEco.DynamicAssembly<Actionable, Context, SelfKnowledge> implementation;
      
      public void start() {
        assert this.agentE != null: "This is a bug.";
        ((EcoRobotAgents.Robot.ComponentImpl<Actionable, Context, SelfKnowledge>) this.agentE).start();
        assert this.aFW != null: "This is a bug.";
        ((Forward.Agent.ComponentImpl<CycleAlert, Context, Actionable>) this.aFW).start();
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
      
      public ComponentImpl(final RobotForwardAssemblyEco.DynamicAssembly<Actionable, Context, SelfKnowledge> implem, final RobotForwardAssemblyEco.DynamicAssembly.Requires<Actionable, Context, SelfKnowledge> b, final boolean doInits) {
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
      
      private EcoRobotAgents.Robot.Component<Actionable, Context, SelfKnowledge> agentE;
      
      private final class BridgeImpl_ecoAE_agentE implements EcoRobotAgents.Robot.Requires<Actionable, Context, SelfKnowledge> {
        public final CycleAlert finishedCycle() {
          return RobotForwardAssemblyEco.DynamicAssembly.ComponentImpl.this.aFW().a();
        }
        
        public final Actionable envInteraction() {
          return RobotForwardAssemblyEco.DynamicAssembly.ComponentImpl.this.aFW().c();
        }
        
        public final Context envContext() {
          return RobotForwardAssemblyEco.DynamicAssembly.ComponentImpl.this.aFW().b();
        }
      }
      
      public final EcoRobotAgents.Robot.Component<Actionable, Context, SelfKnowledge> agentE() {
        return this.agentE;
      }
      
      private Forward.Agent.Component<CycleAlert, Context, Actionable> aFW;
      
      private final class BridgeImpl_fw_aFW implements Forward.Agent.Requires<CycleAlert, Context, Actionable> {
      }
      
      public final Forward.Agent.Component<CycleAlert, Context, Actionable> aFW() {
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
    
    private RobotForwardAssemblyEco.DynamicAssembly.ComponentImpl<Actionable, Context, SelfKnowledge> selfComponent;
    
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
    protected RobotForwardAssemblyEco.DynamicAssembly.Provides<Actionable, Context, SelfKnowledge> provides() {
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
    protected RobotForwardAssemblyEco.DynamicAssembly.Requires<Actionable, Context, SelfKnowledge> requires() {
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
    protected RobotForwardAssemblyEco.DynamicAssembly.Parts<Actionable, Context, SelfKnowledge> parts() {
      assert this.selfComponent != null: "This is a bug.";
      if (!this.init) {
      	throw new RuntimeException("parts() can't be accessed until a component has been created from this implementation, use start() instead of the constructor if parts() is needed to initialise the component.");
      }
      return this.selfComponent;
    }
    
    private EcoRobotAgents.Robot<Actionable, Context, SelfKnowledge> use_agentE;
    
    private Forward.Agent<CycleAlert, Context, Actionable> use_aFW;
    
    /**
     * Not meant to be used to manually instantiate components (except for testing).
     * 
     */
    public synchronized RobotForwardAssemblyEco.DynamicAssembly.Component<Actionable, Context, SelfKnowledge> _newComponent(final RobotForwardAssemblyEco.DynamicAssembly.Requires<Actionable, Context, SelfKnowledge> b, final boolean start) {
      if (this.init) {
      	throw new RuntimeException("This instance of DynamicAssembly has already been used to create a component, use another one.");
      }
      this.init = true;
      RobotForwardAssemblyEco.DynamicAssembly.ComponentImpl<Actionable, Context, SelfKnowledge>  _comp = new RobotForwardAssemblyEco.DynamicAssembly.ComponentImpl<Actionable, Context, SelfKnowledge>(this, b, true);
      if (start) {
      	_comp.start();
      }
      return _comp;
    }
    
    private RobotForwardAssemblyEco.ComponentImpl<Actionable, Context, SelfKnowledge> ecosystemComponent;
    
    /**
     * This can be called by the species implementation to access the provided ports of its ecosystem.
     * 
     */
    protected RobotForwardAssemblyEco.Provides<Actionable, Context, SelfKnowledge> eco_provides() {
      assert this.ecosystemComponent != null: "This is a bug.";
      return this.ecosystemComponent;
    }
    
    /**
     * This can be called by the species implementation to access the required ports of its ecosystem.
     * 
     */
    protected RobotForwardAssemblyEco.Requires<Actionable, Context, SelfKnowledge> eco_requires() {
      assert this.ecosystemComponent != null: "This is a bug.";
      return this.ecosystemComponent.bridge;
    }
    
    /**
     * This can be called by the species implementation to access the parts of its ecosystem and their provided ports.
     * 
     */
    protected RobotForwardAssemblyEco.Parts<Actionable, Context, SelfKnowledge> eco_parts() {
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
  
  private RobotForwardAssemblyEco.ComponentImpl<Actionable, Context, SelfKnowledge> selfComponent;
  
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
  protected RobotForwardAssemblyEco.Provides<Actionable, Context, SelfKnowledge> provides() {
    assert this.selfComponent != null: "This is a bug.";
    if (!this.init) {
    	throw new RuntimeException("provides() can't be accessed until a component has been created from this implementation, use start() instead of the constructor if provides() is needed to initialise the component.");
    }
    return this.selfComponent;
  }
  
  /**
   * This should be overridden by the implementation to define the provided port.
   * This will be called once during the construction of the component to initialize the port.
   * 
   */
  protected abstract ICreateRobot make_createRobot();
  
  /**
   * This can be called by the implementation to access the required ports.
   * 
   */
  protected RobotForwardAssemblyEco.Requires<Actionable, Context, SelfKnowledge> requires() {
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
  protected RobotForwardAssemblyEco.Parts<Actionable, Context, SelfKnowledge> parts() {
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
  protected abstract EcoRobotAgents<Actionable, Context, SelfKnowledge> make_ecoAE();
  
  /**
   * This should be overridden by the implementation to define how to create this sub-component.
   * This will be called once during the construction of the component to initialize this sub-component.
   * 
   */
  protected abstract Forward<CycleAlert, Context, Actionable> make_fw();
  
  /**
   * Not meant to be used to manually instantiate components (except for testing).
   * 
   */
  public synchronized RobotForwardAssemblyEco.Component<Actionable, Context, SelfKnowledge> _newComponent(final RobotForwardAssemblyEco.Requires<Actionable, Context, SelfKnowledge> b, final boolean start) {
    if (this.init) {
    	throw new RuntimeException("This instance of RobotForwardAssemblyEco has already been used to create a component, use another one.");
    }
    this.init = true;
    RobotForwardAssemblyEco.ComponentImpl<Actionable, Context, SelfKnowledge>  _comp = new RobotForwardAssemblyEco.ComponentImpl<Actionable, Context, SelfKnowledge>(this, b, true);
    if (start) {
    	_comp.start();
    }
    return _comp;
  }
  
  /**
   * This should be overridden by the implementation to instantiate the implementation of the species.
   * 
   */
  protected RobotForwardAssemblyEco.DynamicAssembly<Actionable, Context, SelfKnowledge> make_DynamicAssembly(final String id, final Color color, final Cellule position, final Map<Color, Point> nests) {
    return new RobotForwardAssemblyEco.DynamicAssembly<Actionable, Context, SelfKnowledge>();
  }
  
  /**
   * Do not call, used by generated code.
   * 
   */
  public RobotForwardAssemblyEco.DynamicAssembly<Actionable, Context, SelfKnowledge> _createImplementationOfDynamicAssembly(final String id, final Color color, final Cellule position, final Map<Color, Point> nests) {
    RobotForwardAssemblyEco.DynamicAssembly<Actionable, Context, SelfKnowledge> implem = make_DynamicAssembly(id,color,position,nests);
    if (implem == null) {
    	throw new RuntimeException("make_DynamicAssembly() in clrobots.RobotForwardAssemblyEco should not return null.");
    }
    assert implem.ecosystemComponent == null: "This is a bug.";
    assert this.selfComponent != null: "This is a bug.";
    implem.ecosystemComponent = this.selfComponent;
    assert this.selfComponent.implem_ecoAE != null: "This is a bug.";
    assert implem.use_agentE == null: "This is a bug.";
    implem.use_agentE = this.selfComponent.implem_ecoAE._createImplementationOfRobot(id,color,position,nests);
    assert this.selfComponent.implem_fw != null: "This is a bug.";
    assert implem.use_aFW == null: "This is a bug.";
    implem.use_aFW = this.selfComponent.implem_fw._createImplementationOfAgent();
    return implem;
  }
  
  /**
   * This can be called to create an instance of the species from inside the implementation of the ecosystem.
   * 
   */
  protected RobotForwardAssemblyEco.DynamicAssembly.Component<Actionable, Context, SelfKnowledge> newDynamicAssembly(final String id, final Color color, final Cellule position, final Map<Color, Point> nests) {
    RobotForwardAssemblyEco.DynamicAssembly<Actionable, Context, SelfKnowledge> _implem = _createImplementationOfDynamicAssembly(id,color,position,nests);
    return _implem._newComponent(new RobotForwardAssemblyEco.DynamicAssembly.Requires<Actionable, Context, SelfKnowledge>() {},true);
  }
}

package clrobots;

import clrobots.EcoRobot;
import clrobots.EnvEco;
import clrobots.Forward;
import clrobots.interfaces.IEnvInfos;
import clrobots.interfaces.Iinteragir;

@SuppressWarnings("all")
public abstract class Scenario<Runnable> {
  public interface Requires<Runnable> {
  }
  
  public interface Component<Runnable> extends Scenario.Provides<Runnable> {
  }
  
  public interface Provides<Runnable> {
  }
  
  public interface Parts<Runnable> {
    /**
     * This can be called by the implementation to access the part and its provided ports.
     * It will be initialized after the required ports are initialized and before the provided ports are initialized.
     * 
     */
    public EnvEco.Component envEco();
    
    /**
     * This can be called by the implementation to access the part and its provided ports.
     * It will be initialized after the required ports are initialized and before the provided ports are initialized.
     * 
     */
    public EcoRobot.Component<Runnable> ecoRobots();
    
    /**
     * This can be called by the implementation to access the part and its provided ports.
     * It will be initialized after the required ports are initialized and before the provided ports are initialized.
     * 
     */
    public Forward.Component<IEnvInfos, Iinteragir> forward();
  }
  
  public static class ComponentImpl<Runnable> implements Scenario.Component<Runnable>, Scenario.Parts<Runnable> {
    private final Scenario.Requires<Runnable> bridge;
    
    private final Scenario<Runnable> implementation;
    
    public void start() {
      assert this.envEco != null: "This is a bug.";
      ((EnvEco.ComponentImpl) this.envEco).start();
      assert this.ecoRobots != null: "This is a bug.";
      ((EcoRobot.ComponentImpl<Runnable>) this.ecoRobots).start();
      assert this.forward != null: "This is a bug.";
      ((Forward.ComponentImpl<IEnvInfos, Iinteragir>) this.forward).start();
      this.implementation.start();
      this.implementation.started = true;
    }
    
    private void init_envEco() {
      assert this.envEco == null: "This is a bug.";
      assert this.implem_envEco == null: "This is a bug.";
      this.implem_envEco = this.implementation.make_envEco();
      if (this.implem_envEco == null) {
      	throw new RuntimeException("make_envEco() in clrobots.Scenario<Runnable> should not return null.");
      }
      this.envEco = this.implem_envEco._newComponent(new BridgeImpl_envEco(), false);
      
    }
    
    private void init_ecoRobots() {
      assert this.ecoRobots == null: "This is a bug.";
      assert this.implem_ecoRobots == null: "This is a bug.";
      this.implem_ecoRobots = this.implementation.make_ecoRobots();
      if (this.implem_ecoRobots == null) {
      	throw new RuntimeException("make_ecoRobots() in clrobots.Scenario<Runnable> should not return null.");
      }
      this.ecoRobots = this.implem_ecoRobots._newComponent(new BridgeImpl_ecoRobots(), false);
      
    }
    
    private void init_forward() {
      assert this.forward == null: "This is a bug.";
      assert this.implem_forward == null: "This is a bug.";
      this.implem_forward = this.implementation.make_forward();
      if (this.implem_forward == null) {
      	throw new RuntimeException("make_forward() in clrobots.Scenario<Runnable> should not return null.");
      }
      this.forward = this.implem_forward._newComponent(new BridgeImpl_forward(), false);
      
    }
    
    protected void initParts() {
      init_envEco();
      init_ecoRobots();
      init_forward();
    }
    
    protected void initProvidedPorts() {
      
    }
    
    public ComponentImpl(final Scenario<Runnable> implem, final Scenario.Requires<Runnable> b, final boolean doInits) {
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
    
    private EnvEco.Component envEco;
    
    private EnvEco implem_envEco;
    
    private final class BridgeImpl_envEco implements EnvEco.Requires {
    }
    
    public final EnvEco.Component envEco() {
      return this.envEco;
    }
    
    private EcoRobot.Component<Runnable> ecoRobots;
    
    private EcoRobot<Runnable> implem_ecoRobots;
    
    private final class BridgeImpl_ecoRobots implements EcoRobot.Requires<Runnable> {
    }
    
    public final EcoRobot.Component<Runnable> ecoRobots() {
      return this.ecoRobots;
    }
    
    private Forward.Component<IEnvInfos, Iinteragir> forward;
    
    private Forward<IEnvInfos, Iinteragir> implem_forward;
    
    private final class BridgeImpl_forward implements Forward.Requires<IEnvInfos, Iinteragir> {
      public final IEnvInfos i() {
        return Scenario.ComponentImpl.this.envEco().envInfos();
      }
      
      public final Iinteragir j() {
        return Scenario.ComponentImpl.this.envEco().interagir();
      }
    }
    
    public final Forward.Component<IEnvInfos, Iinteragir> forward() {
      return this.forward;
    }
  }
  
  public static class DynamicAssembledRobot<Runnable> {
    public interface Requires<Runnable> {
    }
    
    public interface Component<Runnable> extends Scenario.DynamicAssembledRobot.Provides<Runnable> {
    }
    
    public interface Provides<Runnable> {
    }
    
    public interface Parts<Runnable> {
      /**
       * This can be called by the implementation to access the part and its provided ports.
       * It will be initialized after the required ports are initialized and before the provided ports are initialized.
       * 
       */
      public EcoRobot.Robot.Component<Runnable> rb();
      
      /**
       * This can be called by the implementation to access the part and its provided ports.
       * It will be initialized after the required ports are initialized and before the provided ports are initialized.
       * 
       */
      public Forward.AgentForward.Component<IEnvInfos, Iinteragir> fr();
    }
    
    public static class ComponentImpl<Runnable> implements Scenario.DynamicAssembledRobot.Component<Runnable>, Scenario.DynamicAssembledRobot.Parts<Runnable> {
      private final Scenario.DynamicAssembledRobot.Requires<Runnable> bridge;
      
      private final Scenario.DynamicAssembledRobot<Runnable> implementation;
      
      public void start() {
        assert this.rb != null: "This is a bug.";
        ((EcoRobot.Robot.ComponentImpl<Runnable>) this.rb).start();
        assert this.fr != null: "This is a bug.";
        ((Forward.AgentForward.ComponentImpl<IEnvInfos, Iinteragir>) this.fr).start();
        this.implementation.start();
        this.implementation.started = true;
      }
      
      private void init_rb() {
        assert this.rb == null: "This is a bug.";
        assert this.implementation.use_rb != null: "This is a bug.";
        this.rb = this.implementation.use_rb._newComponent(new BridgeImpl_ecoRobots_rb(), false);
        
      }
      
      private void init_fr() {
        assert this.fr == null: "This is a bug.";
        assert this.implementation.use_fr != null: "This is a bug.";
        this.fr = this.implementation.use_fr._newComponent(new BridgeImpl_forward_fr(), false);
        
      }
      
      protected void initParts() {
        init_rb();
        init_fr();
      }
      
      protected void initProvidedPorts() {
        
      }
      
      public ComponentImpl(final Scenario.DynamicAssembledRobot<Runnable> implem, final Scenario.DynamicAssembledRobot.Requires<Runnable> b, final boolean doInits) {
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
      
      private EcoRobot.Robot.Component<Runnable> rb;
      
      private final class BridgeImpl_ecoRobots_rb implements EcoRobot.Robot.Requires<Runnable> {
        public final IEnvInfos envInfosR() {
          return Scenario.DynamicAssembledRobot.ComponentImpl.this.fr().a();
        }
        
        public final Iinteragir interaction() {
          return Scenario.DynamicAssembledRobot.ComponentImpl.this.fr().b();
        }
      }
      
      public final EcoRobot.Robot.Component<Runnable> rb() {
        return this.rb;
      }
      
      private Forward.AgentForward.Component<IEnvInfos, Iinteragir> fr;
      
      private final class BridgeImpl_forward_fr implements Forward.AgentForward.Requires<IEnvInfos, Iinteragir> {
      }
      
      public final Forward.AgentForward.Component<IEnvInfos, Iinteragir> fr() {
        return this.fr;
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
    
    private Scenario.DynamicAssembledRobot.ComponentImpl<Runnable> selfComponent;
    
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
    protected Scenario.DynamicAssembledRobot.Provides<Runnable> provides() {
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
    protected Scenario.DynamicAssembledRobot.Requires<Runnable> requires() {
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
    protected Scenario.DynamicAssembledRobot.Parts<Runnable> parts() {
      assert this.selfComponent != null: "This is a bug.";
      if (!this.init) {
      	throw new RuntimeException("parts() can't be accessed until a component has been created from this implementation, use start() instead of the constructor if parts() is needed to initialise the component.");
      }
      return this.selfComponent;
    }
    
    private EcoRobot.Robot<Runnable> use_rb;
    
    private Forward.AgentForward<IEnvInfos, Iinteragir> use_fr;
    
    /**
     * Not meant to be used to manually instantiate components (except for testing).
     * 
     */
    public synchronized Scenario.DynamicAssembledRobot.Component<Runnable> _newComponent(final Scenario.DynamicAssembledRobot.Requires<Runnable> b, final boolean start) {
      if (this.init) {
      	throw new RuntimeException("This instance of DynamicAssembledRobot has already been used to create a component, use another one.");
      }
      this.init = true;
      Scenario.DynamicAssembledRobot.ComponentImpl<Runnable>  _comp = new Scenario.DynamicAssembledRobot.ComponentImpl<Runnable>(this, b, true);
      if (start) {
      	_comp.start();
      }
      return _comp;
    }
    
    private Scenario.ComponentImpl<Runnable> ecosystemComponent;
    
    /**
     * This can be called by the species implementation to access the provided ports of its ecosystem.
     * 
     */
    protected Scenario.Provides<Runnable> eco_provides() {
      assert this.ecosystemComponent != null: "This is a bug.";
      return this.ecosystemComponent;
    }
    
    /**
     * This can be called by the species implementation to access the required ports of its ecosystem.
     * 
     */
    protected Scenario.Requires<Runnable> eco_requires() {
      assert this.ecosystemComponent != null: "This is a bug.";
      return this.ecosystemComponent.bridge;
    }
    
    /**
     * This can be called by the species implementation to access the parts of its ecosystem and their provided ports.
     * 
     */
    protected Scenario.Parts<Runnable> eco_parts() {
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
  
  private Scenario.ComponentImpl<Runnable> selfComponent;
  
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
  protected Scenario.Provides<Runnable> provides() {
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
  protected Scenario.Requires<Runnable> requires() {
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
  protected Scenario.Parts<Runnable> parts() {
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
  protected abstract EnvEco make_envEco();
  
  /**
   * This should be overridden by the implementation to define how to create this sub-component.
   * This will be called once during the construction of the component to initialize this sub-component.
   * 
   */
  protected abstract EcoRobot<Runnable> make_ecoRobots();
  
  /**
   * This should be overridden by the implementation to define how to create this sub-component.
   * This will be called once during the construction of the component to initialize this sub-component.
   * 
   */
  protected abstract Forward<IEnvInfos, Iinteragir> make_forward();
  
  /**
   * Not meant to be used to manually instantiate components (except for testing).
   * 
   */
  public synchronized Scenario.Component<Runnable> _newComponent(final Scenario.Requires<Runnable> b, final boolean start) {
    if (this.init) {
    	throw new RuntimeException("This instance of Scenario has already been used to create a component, use another one.");
    }
    this.init = true;
    Scenario.ComponentImpl<Runnable>  _comp = new Scenario.ComponentImpl<Runnable>(this, b, true);
    if (start) {
    	_comp.start();
    }
    return _comp;
  }
  
  /**
   * This should be overridden by the implementation to instantiate the implementation of the species.
   * 
   */
  protected Scenario.DynamicAssembledRobot<Runnable> make_DynamicAssembledRobot() {
    return new Scenario.DynamicAssembledRobot<Runnable>();
  }
  
  /**
   * Do not call, used by generated code.
   * 
   */
  public Scenario.DynamicAssembledRobot<Runnable> _createImplementationOfDynamicAssembledRobot() {
    Scenario.DynamicAssembledRobot<Runnable> implem = make_DynamicAssembledRobot();
    if (implem == null) {
    	throw new RuntimeException("make_DynamicAssembledRobot() in clrobots.Scenario should not return null.");
    }
    assert implem.ecosystemComponent == null: "This is a bug.";
    assert this.selfComponent != null: "This is a bug.";
    implem.ecosystemComponent = this.selfComponent;
    assert this.selfComponent.implem_ecoRobots != null: "This is a bug.";
    assert implem.use_rb == null: "This is a bug.";
    implem.use_rb = this.selfComponent.implem_ecoRobots._createImplementationOfRobot();
    assert this.selfComponent.implem_forward != null: "This is a bug.";
    assert implem.use_fr == null: "This is a bug.";
    implem.use_fr = this.selfComponent.implem_forward._createImplementationOfAgentForward();
    return implem;
  }
  
  /**
   * This can be called to create an instance of the species from inside the implementation of the ecosystem.
   * 
   */
  protected Scenario.DynamicAssembledRobot.Component<Runnable> newDynamicAssembledRobot() {
    Scenario.DynamicAssembledRobot<Runnable> _implem = _createImplementationOfDynamicAssembledRobot();
    return _implem._newComponent(new Scenario.DynamicAssembledRobot.Requires<Runnable>() {},true);
  }
  
  /**
   * Use to instantiate a component from this implementation.
   * 
   */
  public Scenario.Component<Runnable> newComponent() {
    return this._newComponent(new Scenario.Requires<Runnable>() {}, true);
  }
}

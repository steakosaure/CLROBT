package clrobots;

import clrobots.Agir;
import clrobots.Decider;
import clrobots.Percevoir;
import clrobots.interfaces.CycleAlert;
import clrobots.interfaces.Do;
import clrobots.interfaces.ITakeThreads;
import java.awt.Color;

@SuppressWarnings("all")
public abstract class EcoRobots {
  public interface Requires {
    /**
     * This can be called by the implementation to access this required port.
     * 
     */
    public ITakeThreads threads();
  }
  
  public interface Component extends EcoRobots.Provides {
  }
  
  public interface Provides {
  }
  
  public interface Parts {
  }
  
  public static class ComponentImpl implements EcoRobots.Component, EcoRobots.Parts {
    private final EcoRobots.Requires bridge;
    
    private final EcoRobots implementation;
    
    public void start() {
      this.implementation.start();
      this.implementation.started = true;
    }
    
    protected void initParts() {
      
    }
    
    protected void initProvidedPorts() {
      
    }
    
    public ComponentImpl(final EcoRobots implem, final EcoRobots.Requires b, final boolean doInits) {
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
  }
  
  public static abstract class Robot {
    public interface Requires {
      /**
       * This can be called by the implementation to access this required port.
       * 
       */
      public CycleAlert finishedCycle();
    }
    
    public interface Component extends EcoRobots.Robot.Provides {
    }
    
    public interface Provides {
      /**
       * This can be called to access the provided port.
       * 
       */
      public Do launchCycle();
    }
    
    public interface Parts {
      /**
       * This can be called by the implementation to access the part and its provided ports.
       * It will be initialized after the required ports are initialized and before the provided ports are initialized.
       * 
       */
      public Percevoir.Component percevoir();
      
      /**
       * This can be called by the implementation to access the part and its provided ports.
       * It will be initialized after the required ports are initialized and before the provided ports are initialized.
       * 
       */
      public Decider.Component decider();
      
      /**
       * This can be called by the implementation to access the part and its provided ports.
       * It will be initialized after the required ports are initialized and before the provided ports are initialized.
       * 
       */
      public Agir.Component agir();
    }
    
    public static class ComponentImpl implements EcoRobots.Robot.Component, EcoRobots.Robot.Parts {
      private final EcoRobots.Robot.Requires bridge;
      
      private final EcoRobots.Robot implementation;
      
      public void start() {
        assert this.percevoir != null: "This is a bug.";
        ((Percevoir.ComponentImpl) this.percevoir).start();
        assert this.decider != null: "This is a bug.";
        ((Decider.ComponentImpl) this.decider).start();
        assert this.agir != null: "This is a bug.";
        ((Agir.ComponentImpl) this.agir).start();
        this.implementation.start();
        this.implementation.started = true;
      }
      
      private void init_percevoir() {
        assert this.percevoir == null: "This is a bug.";
        assert this.implem_percevoir == null: "This is a bug.";
        this.implem_percevoir = this.implementation.make_percevoir();
        if (this.implem_percevoir == null) {
        	throw new RuntimeException("make_percevoir() in clrobots.EcoRobots$Robot should not return null.");
        }
        this.percevoir = this.implem_percevoir._newComponent(new BridgeImpl_percevoir(), false);
        
      }
      
      private void init_decider() {
        assert this.decider == null: "This is a bug.";
        assert this.implem_decider == null: "This is a bug.";
        this.implem_decider = this.implementation.make_decider();
        if (this.implem_decider == null) {
        	throw new RuntimeException("make_decider() in clrobots.EcoRobots$Robot should not return null.");
        }
        this.decider = this.implem_decider._newComponent(new BridgeImpl_decider(), false);
        
      }
      
      private void init_agir() {
        assert this.agir == null: "This is a bug.";
        assert this.implem_agir == null: "This is a bug.";
        this.implem_agir = this.implementation.make_agir();
        if (this.implem_agir == null) {
        	throw new RuntimeException("make_agir() in clrobots.EcoRobots$Robot should not return null.");
        }
        this.agir = this.implem_agir._newComponent(new BridgeImpl_agir(), false);
        
      }
      
      protected void initParts() {
        init_percevoir();
        init_decider();
        init_agir();
      }
      
      protected void initProvidedPorts() {
        
      }
      
      public ComponentImpl(final EcoRobots.Robot implem, final EcoRobots.Robot.Requires b, final boolean doInits) {
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
      
      public Do launchCycle() {
        return this.percevoir().perception();
      }
      
      private Percevoir.Component percevoir;
      
      private Percevoir implem_percevoir;
      
      private final class BridgeImpl_percevoir implements Percevoir.Requires {
        public final Do decision() {
          return EcoRobots.Robot.ComponentImpl.this.decider().decision();
        }
      }
      
      public final Percevoir.Component percevoir() {
        return this.percevoir;
      }
      
      private Decider.Component decider;
      
      private Decider implem_decider;
      
      private final class BridgeImpl_decider implements Decider.Requires {
        public final Do action() {
          return EcoRobots.Robot.ComponentImpl.this.agir().action();
        }
      }
      
      public final Decider.Component decider() {
        return this.decider;
      }
      
      private Agir.Component agir;
      
      private Agir implem_agir;
      
      private final class BridgeImpl_agir implements Agir.Requires {
        public final CycleAlert finishedCycle() {
          return EcoRobots.Robot.ComponentImpl.this.bridge.finishedCycle();
        }
      }
      
      public final Agir.Component agir() {
        return this.agir;
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
    
    private EcoRobots.Robot.ComponentImpl selfComponent;
    
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
    protected EcoRobots.Robot.Provides provides() {
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
    protected EcoRobots.Robot.Requires requires() {
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
    protected EcoRobots.Robot.Parts parts() {
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
    protected abstract Percevoir make_percevoir();
    
    /**
     * This should be overridden by the implementation to define how to create this sub-component.
     * This will be called once during the construction of the component to initialize this sub-component.
     * 
     */
    protected abstract Decider make_decider();
    
    /**
     * This should be overridden by the implementation to define how to create this sub-component.
     * This will be called once during the construction of the component to initialize this sub-component.
     * 
     */
    protected abstract Agir make_agir();
    
    /**
     * Not meant to be used to manually instantiate components (except for testing).
     * 
     */
    public synchronized EcoRobots.Robot.Component _newComponent(final EcoRobots.Robot.Requires b, final boolean start) {
      if (this.init) {
      	throw new RuntimeException("This instance of Robot has already been used to create a component, use another one.");
      }
      this.init = true;
      EcoRobots.Robot.ComponentImpl  _comp = new EcoRobots.Robot.ComponentImpl(this, b, true);
      if (start) {
      	_comp.start();
      }
      return _comp;
    }
    
    private EcoRobots.ComponentImpl ecosystemComponent;
    
    /**
     * This can be called by the species implementation to access the provided ports of its ecosystem.
     * 
     */
    protected EcoRobots.Provides eco_provides() {
      assert this.ecosystemComponent != null: "This is a bug.";
      return this.ecosystemComponent;
    }
    
    /**
     * This can be called by the species implementation to access the required ports of its ecosystem.
     * 
     */
    protected EcoRobots.Requires eco_requires() {
      assert this.ecosystemComponent != null: "This is a bug.";
      return this.ecosystemComponent.bridge;
    }
    
    /**
     * This can be called by the species implementation to access the parts of its ecosystem and their provided ports.
     * 
     */
    protected EcoRobots.Parts eco_parts() {
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
  
  private EcoRobots.ComponentImpl selfComponent;
  
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
  protected EcoRobots.Provides provides() {
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
  protected EcoRobots.Requires requires() {
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
  protected EcoRobots.Parts parts() {
    assert this.selfComponent != null: "This is a bug.";
    if (!this.init) {
    	throw new RuntimeException("parts() can't be accessed until a component has been created from this implementation, use start() instead of the constructor if parts() is needed to initialise the component.");
    }
    return this.selfComponent;
  }
  
  /**
   * Not meant to be used to manually instantiate components (except for testing).
   * 
   */
  public synchronized EcoRobots.Component _newComponent(final EcoRobots.Requires b, final boolean start) {
    if (this.init) {
    	throw new RuntimeException("This instance of EcoRobots has already been used to create a component, use another one.");
    }
    this.init = true;
    EcoRobots.ComponentImpl  _comp = new EcoRobots.ComponentImpl(this, b, true);
    if (start) {
    	_comp.start();
    }
    return _comp;
  }
  
  /**
   * This should be overridden by the implementation to instantiate the implementation of the species.
   * 
   */
  protected abstract EcoRobots.Robot make_Robot(final String id, final Color color);
  
  /**
   * Do not call, used by generated code.
   * 
   */
  public EcoRobots.Robot _createImplementationOfRobot(final String id, final Color color) {
    EcoRobots.Robot implem = make_Robot(id,color);
    if (implem == null) {
    	throw new RuntimeException("make_Robot() in clrobots.EcoRobots should not return null.");
    }
    assert implem.ecosystemComponent == null: "This is a bug.";
    assert this.selfComponent != null: "This is a bug.";
    implem.ecosystemComponent = this.selfComponent;
    return implem;
  }
}

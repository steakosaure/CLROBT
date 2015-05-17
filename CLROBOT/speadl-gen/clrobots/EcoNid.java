package clrobots;

import clrobot.interfaces.ICreateNid;
import clrobot.interfaces.INidInfo;
import java.awt.Color;

@SuppressWarnings("all")
public abstract class EcoNid {
  public interface Requires {
  }
  
  public interface Component extends EcoNid.Provides {
  }
  
  public interface Provides {
  }
  
  public interface Parts {
  }
  
  public static class ComponentImpl implements EcoNid.Component, EcoNid.Parts {
    private final EcoNid.Requires bridge;
    
    private final EcoNid implementation;
    
    public void start() {
      this.implementation.start();
      this.implementation.started = true;
    }
    
    protected void initParts() {
      
    }
    
    protected void initProvidedPorts() {
      
    }
    
    public ComponentImpl(final EcoNid implem, final EcoNid.Requires b, final boolean doInits) {
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
  
  public static abstract class Nid {
    public interface Requires {
    }
    
    public interface Component extends EcoNid.Nid.Provides {
    }
    
    public interface Provides {
      /**
       * This can be called to access the provided port.
       * 
       */
      public INidInfo nidinfo();
      
      /**
       * This can be called to access the provided port.
       * 
       */
      public ICreateNid creer();
    }
    
    public interface Parts {
    }
    
    public static class ComponentImpl implements EcoNid.Nid.Component, EcoNid.Nid.Parts {
      private final EcoNid.Nid.Requires bridge;
      
      private final EcoNid.Nid implementation;
      
      public void start() {
        this.implementation.start();
        this.implementation.started = true;
      }
      
      protected void initParts() {
        
      }
      
      private void init_nidinfo() {
        assert this.nidinfo == null: "This is a bug.";
        this.nidinfo = this.implementation.make_nidinfo();
        if (this.nidinfo == null) {
        	throw new RuntimeException("make_nidinfo() in clrobots.EcoNid$Nid should not return null.");
        }
      }
      
      private void init_creer() {
        assert this.creer == null: "This is a bug.";
        this.creer = this.implementation.make_creer();
        if (this.creer == null) {
        	throw new RuntimeException("make_creer() in clrobots.EcoNid$Nid should not return null.");
        }
      }
      
      protected void initProvidedPorts() {
        init_nidinfo();
        init_creer();
      }
      
      public ComponentImpl(final EcoNid.Nid implem, final EcoNid.Nid.Requires b, final boolean doInits) {
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
      
      private INidInfo nidinfo;
      
      public INidInfo nidinfo() {
        return this.nidinfo;
      }
      
      private ICreateNid creer;
      
      public ICreateNid creer() {
        return this.creer;
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
    
    private EcoNid.Nid.ComponentImpl selfComponent;
    
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
    protected EcoNid.Nid.Provides provides() {
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
    protected abstract INidInfo make_nidinfo();
    
    /**
     * This should be overridden by the implementation to define the provided port.
     * This will be called once during the construction of the component to initialize the port.
     * 
     */
    protected abstract ICreateNid make_creer();
    
    /**
     * This can be called by the implementation to access the required ports.
     * 
     */
    protected EcoNid.Nid.Requires requires() {
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
    protected EcoNid.Nid.Parts parts() {
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
    public synchronized EcoNid.Nid.Component _newComponent(final EcoNid.Nid.Requires b, final boolean start) {
      if (this.init) {
      	throw new RuntimeException("This instance of Nid has already been used to create a component, use another one.");
      }
      this.init = true;
      EcoNid.Nid.ComponentImpl  _comp = new EcoNid.Nid.ComponentImpl(this, b, true);
      if (start) {
      	_comp.start();
      }
      return _comp;
    }
    
    private EcoNid.ComponentImpl ecosystemComponent;
    
    /**
     * This can be called by the species implementation to access the provided ports of its ecosystem.
     * 
     */
    protected EcoNid.Provides eco_provides() {
      assert this.ecosystemComponent != null: "This is a bug.";
      return this.ecosystemComponent;
    }
    
    /**
     * This can be called by the species implementation to access the required ports of its ecosystem.
     * 
     */
    protected EcoNid.Requires eco_requires() {
      assert this.ecosystemComponent != null: "This is a bug.";
      return this.ecosystemComponent.bridge;
    }
    
    /**
     * This can be called by the species implementation to access the parts of its ecosystem and their provided ports.
     * 
     */
    protected EcoNid.Parts eco_parts() {
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
  
  private EcoNid.ComponentImpl selfComponent;
  
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
  protected EcoNid.Provides provides() {
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
  protected EcoNid.Requires requires() {
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
  protected EcoNid.Parts parts() {
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
  public synchronized EcoNid.Component _newComponent(final EcoNid.Requires b, final boolean start) {
    if (this.init) {
    	throw new RuntimeException("This instance of EcoNid has already been used to create a component, use another one.");
    }
    this.init = true;
    EcoNid.ComponentImpl  _comp = new EcoNid.ComponentImpl(this, b, true);
    if (start) {
    	_comp.start();
    }
    return _comp;
  }
  
  /**
   * This should be overridden by the implementation to instantiate the implementation of the species.
   * 
   */
  protected abstract EcoNid.Nid make_Nid(final Color couleur);
  
  /**
   * Do not call, used by generated code.
   * 
   */
  public EcoNid.Nid _createImplementationOfNid(final Color couleur) {
    EcoNid.Nid implem = make_Nid(couleur);
    if (implem == null) {
    	throw new RuntimeException("make_Nid() in clrobots.EcoNid should not return null.");
    }
    assert implem.ecosystemComponent == null: "This is a bug.";
    assert this.selfComponent != null: "This is a bug.";
    implem.ecosystemComponent = this.selfComponent;
    return implem;
  }
  
  /**
   * This can be called to create an instance of the species from inside the implementation of the ecosystem.
   * 
   */
  protected EcoNid.Nid.Component newNid(final Color couleur) {
    EcoNid.Nid _implem = _createImplementationOfNid(couleur);
    return _implem._newComponent(new EcoNid.Nid.Requires() {},true);
  }
  
  /**
   * Use to instantiate a component from this implementation.
   * 
   */
  public EcoNid.Component newComponent() {
    return this._newComponent(new EcoNid.Requires() {}, true);
  }
}

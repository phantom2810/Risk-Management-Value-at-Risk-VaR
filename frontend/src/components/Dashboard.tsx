import React, { useState, useEffect } from 'react';
import {
  AppBar,
  Toolbar,
  Typography,
  Container,
  Grid,
  Paper,
  Card,
  CardContent,
  Button,
  Box,
  Alert,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  TextField,
  CircularProgress,
  List,
  ListItem,
  ListItemText,
  Chip
} from '@mui/material';
import {
  AccountBalanceWallet,
  CloudUpload,
  TrendingUp,
  Assessment,
  Add,
  Upload
} from '@mui/icons-material';

const Dashboard: React.FC = () => {
  const [createPortfolioOpen, setCreatePortfolioOpen] = useState(false);
  const [portfolios, setPortfolios] = useState<any[]>([]);
  const [loading, setLoading] = useState(false);
  const [portfolioForm, setPortfolioForm] = useState({
    name: '',
    description: '',
    baseCurrency: 'USD'
  });
  const [alert, setAlert] = useState<{type: 'success' | 'error' | 'warning' | 'info', message: string} | null>(null);

  // Fetch portfolios on component mount
  useEffect(() => {
    fetchPortfolios();
  }, []);

  const fetchPortfolios = async () => {
    try {
      const response = await fetch('http://localhost:8080/api/api/portfolio');
      if (response.ok) {
        const data = await response.json();
        setPortfolios(data);
      }
    } catch (error) {
      console.error('Error fetching portfolios:', error);
    }
  };

  const handleCreatePortfolio = async () => {
    setLoading(true);
    try {
      const response = await fetch('http://localhost:8080/api/api/portfolio', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(portfolioForm),
      });

      if (response.ok) {
        const newPortfolio = await response.json();
        setPortfolios([...portfolios, newPortfolio]);
        setCreatePortfolioOpen(false);
        setPortfolioForm({ name: '', description: '', baseCurrency: 'USD' });
        setAlert({ type: 'success', message: 'Portfolio created successfully!' });
        setTimeout(() => setAlert(null), 3000);
      } else {
        setAlert({ type: 'error', message: 'Failed to create portfolio' });
      }
    } catch (error) {
      setAlert({ type: 'error', message: 'Error creating portfolio' });
    }
    setLoading(false);
  };

  const handleFileUpload = () => {
    if (portfolios.length === 0) {
      setAlert({type: 'warning', message: 'Please create a portfolio first before uploading positions.'});
      setTimeout(() => setAlert(null), 3000);
      return;
    }

    const fileInput = document.createElement('input');
    fileInput.type = 'file';
    fileInput.accept = '.csv';
    fileInput.onchange = async (e) => {
      const file = (e.target as HTMLInputElement).files?.[0];
      if (file) {
        setLoading(true);
        try {
          const formData = new FormData();
          formData.append('file', file);
          
          // Use the first portfolio for demo - in real app, user would select
          const portfolioId = portfolios[0].id;
          
          const response = await fetch(`http://localhost:8080/api/api/portfolio/${portfolioId}/positions/upload`, {
            method: 'POST',
            body: formData,
          });

          if (response.ok) {
            const uploadedPositions = await response.json();
            setAlert({ 
              type: 'success', 
              message: `Successfully uploaded ${uploadedPositions.length} positions from ${file.name}!` 
            });
            setTimeout(() => setAlert(null), 5000);
            
            // Refresh portfolio data
            fetchPortfolios();
          } else {
            const errorText = await response.text();
            throw new Error(errorText || 'Failed to upload file');
          }
        } catch (error) {
          console.error('Error uploading file:', error);
          setAlert({ 
            type: 'error', 
            message: 'Failed to upload file. Please check the file format and try again.' 
          });
          setTimeout(() => setAlert(null), 5000);
        } finally {
          setLoading(false);
        }
      }
    };
    fileInput.click();
  };

  const handleCalculateVaR = () => {
    if (portfolios.length === 0) {
      setAlert({ type: 'error', message: 'Please create a portfolio first' });
      setTimeout(() => setAlert(null), 3000);
    } else {
      setAlert({ type: 'success', message: 'VaR calculation functionality coming soon!' });
      setTimeout(() => setAlert(null), 3000);
    }
  };

  const handleViewReports = () => {
    setAlert({ type: 'success', message: 'Reports functionality coming soon!' });
    setTimeout(() => setAlert(null), 3000);
  };
  return (
    <Box sx={{ flexGrow: 1 }}>
      <AppBar position="static">
        <Toolbar>
          <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
            Risk Management & VaR Calculator
          </Typography>
        </Toolbar>
      </AppBar>
      
      <Container maxWidth="lg" sx={{ mt: 4, mb: 4 }}>
        {alert && (
          <Alert severity={alert.type} sx={{ mb: 2 }}>
            {alert.message}
          </Alert>
        )}
        
        <Grid container spacing={3}>
          {/* Welcome Section */}
          <Grid item xs={12}>
            <Paper sx={{ p: 2 }}>
              <Typography variant="h4" gutterBottom>
                Welcome to VaR Calculator
              </Typography>
              <Typography variant="body1" color="text.secondary">
                Calculate Value-at-Risk for your investment portfolios using Historical, 
                Parametric, and Monte Carlo methods.
              </Typography>
              {portfolios.length > 0 && (
                <Box sx={{ mt: 2 }}>
                  <Typography variant="body2" color="primary">
                    You have {portfolios.length} portfolio(s) ready for analysis.
                  </Typography>
                </Box>
              )}
            </Paper>
          </Grid>
          
          {/* Quick Actions */}
          <Grid item xs={12} md={6} lg={3}>
            <Card>
              <CardContent>
                <Box sx={{ display: 'flex', alignItems: 'center', mb: 2 }}>
                  <AccountBalanceWallet sx={{ mr: 2, color: 'primary.main' }} />
                  <Typography variant="h6">Create Portfolio</Typography>
                </Box>
                <Typography variant="body2" color="text.secondary" sx={{ mb: 2 }}>
                  Create a new investment portfolio to start risk analysis
                </Typography>
                <Button 
                  variant="contained" 
                  fullWidth
                  onClick={() => setCreatePortfolioOpen(true)}
                  startIcon={<Add />}
                >
                  Create Portfolio
                </Button>
              </CardContent>
            </Card>
          </Grid>
          
          <Grid item xs={12} md={6} lg={3}>
            <Card>
              <CardContent>
                <Box sx={{ display: 'flex', alignItems: 'center', mb: 2 }}>
                  <CloudUpload sx={{ mr: 2, color: 'secondary.main' }} />
                  <Typography variant="h6">Upload Positions</Typography>
                </Box>
                <Typography variant="body2" color="text.secondary" sx={{ mb: 2 }}>
                  Upload portfolio positions from CSV file
                </Typography>
                <Button 
                  variant="contained" 
                  color="secondary" 
                  fullWidth
                  onClick={handleFileUpload}
                  startIcon={<Upload />}
                >
                  Upload CSV
                </Button>
              </CardContent>
            </Card>
          </Grid>
          
          <Grid item xs={12} md={6} lg={3}>
            <Card>
              <CardContent>
                <Box sx={{ display: 'flex', alignItems: 'center', mb: 2 }}>
                  <TrendingUp sx={{ mr: 2, color: 'success.main' }} />
                  <Typography variant="h6">Calculate VaR</Typography>
                </Box>
                <Typography variant="body2" color="text.secondary" sx={{ mb: 2 }}>
                  Run VaR calculations using different methodologies
                </Typography>
                <Button 
                  variant="contained" 
                  color="success" 
                  fullWidth
                  onClick={handleCalculateVaR}
                >
                  Calculate VaR
                </Button>
              </CardContent>
            </Card>
          </Grid>
          
          <Grid item xs={12} md={6} lg={3}>
            <Card>
              <CardContent>
                <Box sx={{ display: 'flex', alignItems: 'center', mb: 2 }}>
                  <Assessment sx={{ mr: 2, color: 'warning.main' }} />
                  <Typography variant="h6">View Reports</Typography>
                </Box>
                <Typography variant="body2" color="text.secondary" sx={{ mb: 2 }}>
                  View and download risk analysis reports
                </Typography>
                <Button 
                  variant="contained" 
                  color="warning" 
                  fullWidth
                  onClick={handleViewReports}
                >
                  View Reports
                </Button>
              </CardContent>
            </Card>
          </Grid>
          
          {/* Recent Activity */}
          <Grid item xs={12} md={8}>
            <Paper sx={{ p: 2 }}>
              <Typography variant="h6" gutterBottom>
                Your Portfolios
              </Typography>
              {portfolios.length === 0 ? (
                <Typography variant="body2" color="text.secondary">
                  No portfolios created yet. Click "Create Portfolio" to get started.
                </Typography>
              ) : (
                <List>
                  {portfolios.map((portfolio) => (
                    <ListItem key={portfolio.id} sx={{ px: 0 }}>
                      <ListItemText
                        primary={portfolio.name}
                        secondary={
                          <Box>
                            <Typography variant="body2" color="text.secondary">
                              {portfolio.description}
                            </Typography>
                            {portfolio.positionCount > 0 && (
                              <Typography variant="caption" color="primary">
                                {portfolio.positionCount} position(s)
                                {portfolio.totalMarketValue && 
                                  ` • Total Value: ${portfolio.baseCurrency} ${portfolio.totalMarketValue.toLocaleString()}`
                                }
                              </Typography>
                            )}
                          </Box>
                        }
                      />
                      <Chip 
                        label={portfolio.baseCurrency} 
                        size="small" 
                        color="primary" 
                        variant="outlined"
                      />
                    </ListItem>
                  ))}
                </List>
              )}
            </Paper>
          </Grid>
          
          {/* Risk Summary */}
          <Grid item xs={12} md={4}>
            <Paper sx={{ p: 2 }}>
              <Typography variant="h6" gutterBottom>
                Risk Summary
              </Typography>
              <Typography variant="body2" color="text.secondary">
                Portfolio risk metrics will appear here after calculations.
              </Typography>
            </Paper>
          </Grid>
        </Grid>

        {/* Create Portfolio Dialog */}
        <Dialog open={createPortfolioOpen} onClose={() => setCreatePortfolioOpen(false)} maxWidth="sm" fullWidth>
          <DialogTitle>Create New Portfolio</DialogTitle>
          <DialogContent>
            <TextField
              autoFocus
              margin="dense"
              label="Portfolio Name"
              fullWidth
              variant="outlined"
              value={portfolioForm.name}
              onChange={(e) => setPortfolioForm({...portfolioForm, name: e.target.value})}
              sx={{ mb: 2 }}
            />
            <TextField
              margin="dense"
              label="Description"
              fullWidth
              multiline
              rows={3}
              variant="outlined"
              value={portfolioForm.description}
              onChange={(e) => setPortfolioForm({...portfolioForm, description: e.target.value})}
              sx={{ mb: 2 }}
            />
            <TextField
              margin="dense"
              label="Base Currency"
              fullWidth
              variant="outlined"
              value={portfolioForm.baseCurrency}
              onChange={(e) => setPortfolioForm({...portfolioForm, baseCurrency: e.target.value})}
            />
          </DialogContent>
          <DialogActions>
            <Button onClick={() => setCreatePortfolioOpen(false)}>Cancel</Button>
            <Button 
              onClick={handleCreatePortfolio}
              variant="contained"
              disabled={loading || !portfolioForm.name.trim()}
            >
              {loading ? <CircularProgress size={20} /> : 'Create Portfolio'}
            </Button>
          </DialogActions>
        </Dialog>
      </Container>
    </Box>
  );
};

export default Dashboard;
